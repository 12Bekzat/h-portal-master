package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Signature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentRequest document;
    @ManyToOne
    @JoinColumn(name = "signed_by_id")
    private User signedBy;
    private LocalDateTime signedAt;
    private String signatureHash; // может быть путь к ЭЦП или хэш
    private String stampImagePath; // путь к изображению печати
}
