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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
