package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    List<Ciudad> findByPaisIdOrderByNombreAsc(Long paisId);
}
