package com.example.demo.catalogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "IMAGEN_LUGAR")
public class ImagenLugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGEN_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LUGAR_ID")
    @JsonIgnore
    private Lugar lugar;

    @Column(name = "URL", nullable = false, length = 1000)
    private String url;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Lugar getLugar() { return lugar; }
    public void setLugar(Lugar lugar) { this.lugar = lugar; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
