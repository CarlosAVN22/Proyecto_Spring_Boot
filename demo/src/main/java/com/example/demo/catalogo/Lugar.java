package com.example.demo.catalogo;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "LUGAR")
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LUGAR_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORIA_ID")
    private CategoriaLugar categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PAIS_ID")
    private Pais pais;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CIUDAD_ID")
    private Ciudad ciudad;

    @Column(name = "NOMBRE", nullable = false, length = 200)
    private String nombre;

    @Column(name = "DIRECCION", length = 300)
    private String direccion;

    @Column(name = "PRECIO")
    private BigDecimal precio;

    @Column(name = "PROMEDIO_ESTRELLAS")
    private BigDecimal promedioEstrellas;

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CategoriaLugar getCategoria() { return categoria; }
    public void setCategoria(CategoriaLugar categoria) { this.categoria = categoria; }

    public Pais getPais() { return pais; }
    public void setPais(Pais pais) { this.pais = pais; }

    public Ciudad getCiudad() { return ciudad; }
    public void setCiudad(Ciudad ciudad) { this.ciudad = ciudad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public BigDecimal getPromedioEstrellas() { return promedioEstrellas; }
    public void setPromedioEstrellas(BigDecimal promedioEstrellas) { this.promedioEstrellas = promedioEstrellas; }
}
