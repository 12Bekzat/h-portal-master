package com.example.demo.DTO;

import lombok.Data;

@Data
public class DocumentMessageCreateDto {
    private Long documentId;
    private Long senderId;
    private String content;
}

