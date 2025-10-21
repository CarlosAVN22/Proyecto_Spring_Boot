package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LugarDetalleRepository extends Repository<Lugar, Long> {

    @Query(value = """
      SELECT
        l.lugar_id AS lugarId,
        l.nombre   AS lugar,
        l.direccion AS direccion,
        l.precio   AS precio,
        l.promedio_estrellas AS promedioEstrellas,
        cat.nombre AS categoria,
        cat.codigo AS categoriaCodigo,
        p.nombre   AS pais,
        c.nombre   AS ciudad
      FROM lugar l
      JOIN categoria_lugar cat ON cat.categoria_id = l.categoria_id
      JOIN pais p              ON p.pais_id        = l.pais_id
      LEFT JOIN ciudad c       ON c.ciudad_id      = l.ciudad_id
      WHERE l.lugar_id = :id
    """, nativeQuery = true)
    Optional<LugarDetalleRow> obtenerDetalle(@Param("id") Long id);
}
