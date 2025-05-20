package com.example.demo.Repository;

import com.example.demo.Models.DocumentTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDocumentTemplateRepository  extends JpaRepository<DocumentTemplate, Long> {
    Optional<DocumentTemplate> findByName(String name);
}
