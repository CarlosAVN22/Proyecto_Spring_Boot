package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaLugarRepository extends JpaRepository<CategoriaLugar, Long> {
    Optional<CategoriaLugar> findByCodigo(String codigo);
}
