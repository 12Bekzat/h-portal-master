package com.example.demo.DTO;

import com.example.demo.Models.DocumentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentRequestDto {
    private Long id;
    private String templateName;
    private DocumentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    private boolean hasSignature;
}

