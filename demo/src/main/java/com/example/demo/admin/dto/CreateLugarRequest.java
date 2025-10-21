package com.example.demo.admin.dto;

import java.math.BigDecimal;

public record CreateLugarRequest(
        String nombre,
        String categoriaCodigo,
        Long paisId,
        Long ciudadId,        // opcional
        String direccion,     // opcional
        BigDecimal precio,    // opcional
        String imagenUrl      // opcional
) {}
