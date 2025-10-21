package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImagenLugarRepository extends JpaRepository<ImagenLugar, Long> {

    // Ya lo usamos en el AdminApiController
    Optional<ImagenLugar> findFirstByLugarIdOrderByIdAsc(Long lugarId);

    // Este es el que pide tu LugarController:
    @Query("select i.url from ImagenLugar i where i.lugar.id = :lugarId order by i.id asc")
    List<String> listarUrls(@Param("lugarId") Long lugarId);
}
