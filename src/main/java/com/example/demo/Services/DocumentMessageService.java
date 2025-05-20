package com.example.demo.Services;

import com.example.demo.DTO.DocumentMessageCreateDto;
import com.example.demo.DTO.DocumentMessageDto;
import com.example.demo.Models.DocumentMessage;
import com.example.demo.Models.DocumentRequest;
import com.example.demo.Models.User;
import com.example.demo.Repository.IDocMessageRepo;
import com.example.demo.Repository.IDocRequestRepo;
import com.example.demo.Repository.IUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentMessageService {

    private final IDocMessageRepo messageRepository;
    private final IUserRepository userRepository;
    private final IDocRequestRepo requestRepository;

    public DocumentMessageDto sendMessage(DocumentMessageCreateDto dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        DocumentRequest request = requestRepository.findById(dto.getDocumentId())
                .orElseThrow(() -> new EntityNotFoundException("Document not found"));

        DocumentMessage message = new DocumentMessage();
        message.setDocument(request);
        message.setSender(sender);
        message.setContent(dto.getContent());
        message.setSentAt(LocalDateTime.now());

        message = messageRepository.save(message);
        return toDto(message);
    }

    public List<DocumentMessageDto> getMessages(Long documentId) {
        List<DocumentMessage> messages = messageRepository.findByDocumentIdOrderBySentAtAsc(documentId);
        return messages.stream().map(this::toDto).toList();
    }

    public List<DocumentMessageDto> getMessages(Long userId, Long senderId) {
        List<DocumentMessage> messages = messageRepository.findAll();
        return messages.stream()
                .filter(message -> message.getReceiver().getId().equals(userId) && message.getReceiver().getId().equals(senderId))
                .map(this::toDto).toList();
    }

    private DocumentMessageDto toDto(DocumentMessage message) {
        DocumentMessageDto dto = new DocumentMessageDto();
        dto.setId(message.getId());
        dto.setDocumentId(message.getDocument().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setSentAt(message.getSentAt());
        return dto;
    }
}
