package com.example.demo.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIA_LUGAR")
public class CategoriaLugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORIA_ID")
    private Long id;

    @Column(name = "CODIGO", nullable = false, unique = true, length = 30)
    private String codigo; // HOTEl, PLAYA, RESTAURANTE...

    @Column(name = "NOMBRE", nullable = false, length = 80)
    private String nombre;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
