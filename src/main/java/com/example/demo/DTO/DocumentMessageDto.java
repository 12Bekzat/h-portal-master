package com.example.demo.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class DocumentMessageDto {
    private Long id;
    private Long documentId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime sentAt;
}
