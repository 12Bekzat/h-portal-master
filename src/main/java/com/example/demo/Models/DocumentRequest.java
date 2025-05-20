package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DocumentRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private DocumentTemplate template;
    private LocalDateTime createdAt;
    private DocumentStatus status;
    private String rejectionReason;
    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private User approvedBy; // может быть null
    private LocalDateTime approvedAt;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "filled_content")
    private String filledContent; // с подставленными данными
    private String extra;
    private boolean hasSignature;
}
