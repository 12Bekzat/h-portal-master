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
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentRequestService {

    private final IDocRequestRepo requestRepository;
    private final IDocumentTemplateRepository templateRepository;
    private final IUserRepository userRepository;
    private final ISignatureRepo signatureRepository;

    public DocumentRequestDto createRequest(DocumentRequestCreateDto dto) {
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

        // Примитивная генерация — просто замена {{fullName}} и т.п.
        String filled = template.getContentTemplate()
                .replace("{{fullName}}", user.getUsername());
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

        requestRepository.save(request);
    }

    public void reject(Long requestId, String reason) {
        DocumentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        request.setStatus(DocumentStatus.REJECTED);
        request.setRejectionReason(reason);

        requestRepository.save(request);
    }

    public byte[] generatePdf(Long requestId) {
        DocumentRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        String content = request.getFilledContent();

        // Здесь подставляется примитивный генератор PDF
        // Реализация может быть основана на iText, OpenPDF, Apache PDFBox и т.д.
        return PdfGenerator.generate(content); // stub method
    }

    public List<DocumentRequestDto> getAll(Long userId) {
        List<DocumentRequest> requests;
        if (userId != null) {
            requests = requestRepository.findByRequesterId(userId);
        } else {
            requests = requestRepository.findAll();
        }
        return requests.stream().map(this::toDto).toList();
    }

    private DocumentRequestDto toDto(DocumentRequest request) {
        DocumentRequestDto dto = new DocumentRequestDto();
        dto.setId(request.getId());
        dto.setTemplateName(request.getTemplate().getName());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setApprovedAt(request.getApprovedAt());
        dto.setRejectionReason(request.getRejectionReason());
        dto.setHasSignature(request.isHasSignature());
        return dto;
    }
}