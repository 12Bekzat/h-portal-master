package com.example.demo.Services;

import com.example.demo.DTO.DocumentRequestCreateDto;
import com.example.demo.DTO.DocumentRequestDto;
import com.example.demo.Models.DocumentRequest;
import com.example.demo.Models.DocumentStatus;
import com.example.demo.Models.DocumentTemplate;
import com.example.demo.Models.User;
import com.example.demo.Repository.IDocRequestRepo;
import com.example.demo.Repository.IDocumentTemplateRepository;
import com.example.demo.Repository.ISignatureRepo;
import com.example.demo.Repository.IUserRepository;
import com.example.demo.Utils.PdfGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentRequestService {

    private final IDocRequestRepo requestRepository;
    private final IDocumentTemplateRepository templateRepository;
    private final IUserRepository userRepository;
    private final ISignatureRepo signatureRepository;
    private final EmailService emailService;

    public DocumentRequestDto createRequest(DocumentRequestCreateDto dto) throws JsonProcessingException {
        User user = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DocumentTemplate template = templateRepository.findById(dto.getTemplateId())
                .orElseThrow(() -> new EntityNotFoundException("Template not found"));

        DocumentRequest request = new DocumentRequest();
        request.setRequester(user);
        request.setTemplate(template);
        request.setCreatedAt(LocalDateTime.now());

        if (template.isRequiresApproval()) {
            request.setStatus(DocumentStatus.PENDING);
        } else {
            request.setStatus(DocumentStatus.AUTO_ISSUED);
            request.setApprovedAt(LocalDateTime.now());
        }

        String filled = template.getContentTemplate();
        if (user.getExtra() != null) {
            switch (template.getName()) {
                case "place_of_study": {

                    if (user.getExtra().isEmpty()) break;
                    if (!user.getExtra().contains("courseName")) break;
                    if (!user.getExtra().contains("courseYear")) break;

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(user.getExtra());

                    String courseName = root.get("courseName").asText();
                    String courseYear = root.get("courseYear").asText();

                    Map<String, String> values = Map.of(
                            "fullName", user.getFullName(),
                            "courseName", courseName,
                            "courseYear", courseYear,
                            "requestDate", LocalDateTime.now().toLocalDate().toString()
                    );

                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        filled = filled.replace("{{" + entry.getKey() + "}}", entry.getValue());
                    }
                    break;
                }
                case "place_of_work": {

                    if (!user.getExtra().contains("position")) break;
                    if (!user.getExtra().contains("department")) break;
                    if (!user.getExtra().contains("employmentStartDate")) break;

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(user.getExtra());

                    String courseName = root.get("position").asText();
                    String courseYear = root.get("department").asText();
                    String employmentStartDate = root.get("employmentStartDate").asText();

                    Map<String, String> values = Map.of(
                            "fullName", user.getFullName(),
                            "position", courseName,
                            "department", courseYear,
                            "employmentStartDate", employmentStartDate,
                            "requestDate", LocalDateTime.now().toLocalDate().toString()
                    );

                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        filled = filled.replace("{{" + entry.getKey() + "}}", entry.getValue());
                    }
                    break;
                }
                case "application_vacation": {

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(user.getExtra());
                    JsonNode documentRoot = mapper.readTree(dto.getExtra());

                    String startDate = documentRoot.get("startDate").asText();
                    String endDate = documentRoot.get("endDate").asText();
                    String reason = documentRoot.get("reason").asText();
                    String position = root.get("position").asText();

                    Map<String, String> values = Map.of(
                            "startDate", startDate,
                            "endDate", endDate,
                            "reason", reason,
                            "fullName", user.getFullName(),
                            "position", position,
                            "requestDate", LocalDateTime.now().toLocalDate().toString()
                    );

                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        filled = filled.replace("{{" + entry.getKey() + "}}", entry.getValue());
                    }
                    break;
                }
            }
        }
        request.setFilledContent(filled);

        request = requestRepository.save(request);

        return toDto(request);
    }

    public void approve(Long requestId, Long approverId) {
        DocumentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new EntityNotFoundException("Approver not found"));

        request.setStatus(DocumentStatus.APPROVED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        String filledContent = request.getFilledContent();
        filledContent = filledContent.replace("{{approvedText}}", "одобрена");
        filledContent = filledContent.replace("{{approvalDate}}", "Дата утверждения: " + LocalDateTime.now().toLocalDate().toString());
        request.setFilledContent(filledContent);

        requestRepository.save(request);

        emailService.sendSimpleEmail(request.getRequester().getEmail(),
                "Ваш запрос на получение документа",
                "Ваш запрос на получения " + getTitleOfDocument(request.getTemplate().getName()) + " было одобрено!");
    }

    private String getTitleOfDocument(String name) {
        if (name.equals("place_of_work")) return "Справка с место работы";
        else if (name.equals("place_of_study")) return "Справка с место учебы";
        else return "Заявление на отпуск";
    }

    public void reject(Long requestId, String reason) {
        DocumentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(DocumentStatus.REJECTED);
        request.setRejectionReason(reason);
        String filledContent = request.getFilledContent();
        filledContent = filledContent.replace("{{approvedText}}", "отклонена");
        filledContent = filledContent.replace("{{approvalDate}}", "");
        request.setFilledContent(filledContent);

        requestRepository.save(request);

        emailService.sendSimpleEmail(request.getRequester().getEmail(),
                "Ваш запрос на получение документа",
                "Ваш запрос на получения " + getTitleOfDocument(request.getTemplate().getName()) + " было отказано!");
    }

    public byte[] generatePdf(Long requestId) {
        DocumentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        String content = request.getFilledContent();

        // Здесь подставляется примитивный генератор PDF
        // Реализация может быть основана на iText, OpenPDF, Apache PDFBox и т.д.
        return PdfGenerator.generate(content); // stub method
    }

    @Transactional
    public List<DocumentRequestDto> getAll(Long userId) {
        List<DocumentRequest> requests;
        if (userId != null) {
            requests = requestRepository.findAll().stream().filter(request -> request.getRequester().getId().equals(userId)).collect(Collectors.toList());
        } else {
            requests = requestRepository.findAll();
        }
        return requests.stream().map(this::toDto).toList();
    }

    private DocumentRequestDto toDto(DocumentRequest request) {
        DocumentRequestDto dto = new DocumentRequestDto();
        dto.setId(request.getId());
        dto.setTemplateName(request.getTemplate().getName());
        dto.setRequesterFullName(request.getRequester().getFullName());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setApprovedAt(request.getApprovedAt());
        dto.setRejectionReason(request.getRejectionReason());
        dto.setHasSignature(request.isHasSignature());
        return dto;
    }
}