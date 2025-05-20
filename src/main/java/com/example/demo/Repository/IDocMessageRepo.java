package com.example.demo.Repository;

import com.example.demo.Models.DocumentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDocMessageRepo  extends JpaRepository<DocumentMessage, Long> {
    List<DocumentMessage> findByDocumentIdOrderBySentAtAsc(Long documentId);
}