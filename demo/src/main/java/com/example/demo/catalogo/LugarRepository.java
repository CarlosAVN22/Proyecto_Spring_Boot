package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LugarRepository extends JpaRepository<Lugar, Long> {
    List<Lugar> findByPaisIdOrderByNombreAsc(Long paisId);
    List<Lugar> findByNombreContainingIgnoreCase(String q);
    List<Lugar> findByPaisIdAndNombreContainingIgnoreCase(Long paisId, String q);
    List<Lugar> findTop50ByOrderByIdDesc();
}
