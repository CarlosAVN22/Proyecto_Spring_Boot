package com.example.demo.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "LUGAR")
public class Lugar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LUGAR_ID")
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
