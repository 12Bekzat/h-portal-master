package com.example.demo.DTO;

import com.example.demo.Models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class CreateMessageTextDto {
    private String text;
    private Long senderId;
    private Long receiverId;
}
