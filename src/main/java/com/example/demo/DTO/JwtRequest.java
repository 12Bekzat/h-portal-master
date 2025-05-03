package com.example.demo.DTO;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
