package com.example.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DocumentMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "document_id")
    private DocumentRequest document;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;
    private String content;
    private LocalDateTime sentAt;
}
