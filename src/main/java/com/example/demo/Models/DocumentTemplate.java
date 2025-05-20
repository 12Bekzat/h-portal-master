package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DocumentTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name; // Название шаблона, например "Справка с места учёбы"
    private String description;
    private boolean requiresApproval;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "content_template")
    private String contentTemplate;
}
