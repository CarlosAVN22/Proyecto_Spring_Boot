package com.example.demo.web.dto;

import java.math.BigDecimal;

public class LugarDetalleDTO {
    private Long id;
    private String nombre;

    private Long paisId;
    private String pais;

    private Long categoriaId;
    private String categoria;

    private String direccion;
    private BigDecimal precio;

    public LugarDetalleDTO(Long id, String nombre,
                           Long paisId, String pais,
                           Long categoriaId, String categoria,
                           String direccion, BigDecimal precio) {
        this.id = id;
        this.nombre = nombre;
        this.paisId = paisId;
        this.pais = pais;
        this.categoriaId = categoriaId;
        this.categoria = categoria;
        this.direccion = direccion;
        this.precio = precio;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public Long getPaisId() { return paisId; }
    public String getPais() { return pais; }
    public Long getCategoriaId() { return categoriaId; }
    public String getCategoria() { return categoria; }
    public String getDireccion() { return direccion; }
    public BigDecimal getPrecio() { return precio; }
}
