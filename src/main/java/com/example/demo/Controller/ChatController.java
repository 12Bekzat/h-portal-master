package com.example.demo.Controller;

import com.example.demo.DTO.CreateMessageTextDto;
import com.example.demo.DTO.DocumentMessageCreateDto;
import com.example.demo.DTO.DocumentMessageDto;
import com.example.demo.DTO.MessageTextDto;
import com.example.demo.Models.MessageText;
import com.example.demo.Services.ChatService;
import com.example.demo.Services.DocumentMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private DocumentMessageService messageService;
    @Autowired
    private ChatService chatService;

    // Отправить сообщение по документу
    @PostMapping("/send")
    public ResponseEntity<MessageText> sendMessage(@RequestBody CreateMessageTextDto dto) {
        MessageText messageText = chatService.sendMessage(dto.getReceiverId(), dto.getSenderId(), dto.getText());
        return ResponseEntity.ok(messageText);
    }

    // Получить переписку по заявке
    @PostMapping("/{documentId}")
    public ResponseEntity<List<DocumentMessageDto>> getMessages(@PathVariable Long documentId) {
        return ResponseEntity.ok(messageService.getMessages(documentId));
    }

    @PostMapping("/getPaged")
    public ResponseEntity<List<MessageText>> getMessages(@RequestBody MessageTextDto messageTextDto) {
        return ResponseEntity.ok(chatService.getMessages(messageTextDto.getReceiverId(), messageTextDto.getSenderId()));
    }

}
