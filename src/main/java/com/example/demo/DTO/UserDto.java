package com.example.demo.DTO;

import com.example.demo.Models.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private List<String> roles;
    private String url;
    private String extra;
}
