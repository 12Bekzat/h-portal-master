package com.example.demo.Repository;

import com.example.demo.Models.MessageText;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageTextRepo extends JpaRepository<MessageText, Long> {
}
