package com.example.demo.Controller;

import com.example.demo.Models.DocumentTemplate;
import com.example.demo.Models.User;
import com.example.demo.Repository.IDocumentTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentTemplateController {
    private final IDocumentTemplateRepository documentTemplateRepository;

    @PostMapping("/api/templates/getPaged")
    public ResponseEntity<List<DocumentTemplate>> getPaged() {
        return ResponseEntity.ok(documentTemplateRepository.findAll());
    }

    @PostMapping("/api/templates/getById/{id}")
    public ResponseEntity<DocumentTemplate> getById(@PathVariable Long id) {
        return ResponseEntity.ok(documentTemplateRepository.findById(id).get());
    }
}
