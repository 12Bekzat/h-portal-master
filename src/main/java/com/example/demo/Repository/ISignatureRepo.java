package com.example.demo.Repository;

import com.example.demo.Models.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISignatureRepo extends JpaRepository<Signature, Long> {
    Optional<Signature> findByDocumentId(Long documentId);
}
