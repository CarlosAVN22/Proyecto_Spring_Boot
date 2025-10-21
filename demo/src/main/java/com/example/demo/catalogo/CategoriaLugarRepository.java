package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaLugarRepository extends JpaRepository<CategoriaLugar, Long> {

    // Mantengo el tuyo por compatibilidad
    Optional<CategoriaLugar> findByCodigo(String codigo);

    // Recomendado: tolera mayúsculas/minúsculas y espacios del front
    Optional<CategoriaLugar> findByCodigoIgnoreCase(String codigo);
}
