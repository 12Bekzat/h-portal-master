package com.example.demo.Controller;

import com.example.demo.DTO.DocumentRequestCreateDto;
import com.example.demo.DTO.DocumentRequestDto;
import com.example.demo.Services.DocumentRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentRequestController {

    private final DocumentRequestService documentRequestService;

    // Создание новой заявки на документ
    @PostMapping("/request")
    public ResponseEntity<DocumentRequestDto> createDocumentRequest(@RequestBody DocumentRequestCreateDto dto) {
        DocumentRequestDto created = documentRequestService.createRequest(dto);
        return ResponseEntity.ok(created);
    }

    // Подтверждение заявки
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveDocument(@PathVariable Long id, @RequestParam Long approverId) {
        documentRequestService.approve(id, approverId);
        return ResponseEntity.ok().build();
    }

    // Отклонение заявки
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectDocument(@PathVariable Long id, @RequestParam String reason) {
        documentRequestService.reject(id, reason);
        return ResponseEntity.ok().build();
    }

    // Генерация PDF-документа
    @GetMapping("/{id}/generate")
    public ResponseEntity<byte[]> generateDocument(@PathVariable Long id) {
        byte[] pdf = documentRequestService.generatePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    // Получить список заявок пользователя или всех (если admin)
    @GetMapping
    public ResponseEntity<List<DocumentRequestDto>> getAll(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(documentRequestService.getAll(userId));
    }
}
