package com.example.demo.Repository;

import com.example.demo.Models.DocumentRequest;
import com.example.demo.Models.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IDocRequestRepo extends JpaRepository<DocumentRequest, Long> {
    List<DocumentRequest> findByRequesterId(Long requesterId);
    List<DocumentRequest> findByStatus(DocumentStatus status);
}

