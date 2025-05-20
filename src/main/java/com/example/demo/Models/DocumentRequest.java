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

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public DocumentTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DocumentTemplate template) {
        this.template = template;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getFilledContent() {
        return filledContent;
    }

    public void setFilledContent(String filledContent) {
        this.filledContent = filledContent;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isHasSignature() {
        return hasSignature;
    }

    public void setHasSignature(boolean hasSignature) {
        this.hasSignature = hasSignature;
    }
}
