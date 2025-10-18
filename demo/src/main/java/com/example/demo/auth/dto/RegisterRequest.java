package com.example.demo.auth.dto;

public record RegisterRequest(
        String nombre,
        String apellido,
        String email,
        String password,
        String dui,
        String telefono
) {}
