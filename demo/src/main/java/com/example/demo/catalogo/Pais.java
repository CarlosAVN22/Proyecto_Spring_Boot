package com.example.demo.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "PAIS")
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAIS_ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(name = "CODIGO_PAIS", nullable = false, unique = true, length = 3)
    private String codigoPais; // 'SLV','GTM','HND','NIC','CRI','PAN','BLZ'

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigoPais() { return codigoPais; }
    public void setCodigoPais(String codigoPais) { this.codigoPais = codigoPais; }
}
