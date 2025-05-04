package com.example.demo.Controller;

import com.example.demo.DTO.DocumentMessageCreateDto;
import com.example.demo.DTO.DocumentMessageDto;
import com.example.demo.Services.DocumentMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {

    private final DocumentMessageService messageService;

    // Отправить сообщение по документу
    @PostMapping("/send")
    public ResponseEntity<DocumentMessageDto> sendMessage(@RequestBody DocumentMessageCreateDto dto) {
        DocumentMessageDto sent = messageService.sendMessage(dto);
        return ResponseEntity.ok(sent);
    }

    // Получить переписку по заявке
    @GetMapping("/{documentId}")
    public ResponseEntity<List<DocumentMessageDto>> getMessages(@PathVariable Long documentId) {
        return ResponseEntity.ok(messageService.getMessages(documentId));
    }
}
