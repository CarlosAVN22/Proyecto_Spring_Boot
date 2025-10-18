package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LugarResumenRepository extends Repository<Pais, Long> {
    List<Pais> findAll(); // dummy

    @Query(value = """
            SELECT
              l.lugar_id AS lugarId,
              p.nombre   AS pais,
              c.nombre   AS ciudad,
              cat.codigo AS categoriaCodigo,
              cat.nombre AS categoria,
              l.nombre   AS lugar,
              l.direccion AS direccion,
              l.precio   AS precio,
              l.promedio_estrellas AS promedioEstrellas,
              (SELECT url FROM imagen_lugar i
                 WHERE i.lugar_id = l.lugar_id
                 FETCH FIRST 1 ROWS ONLY) AS imagenPrincipal
            FROM lugar l
            JOIN categoria_lugar cat ON cat.categoria_id = l.categoria_id
            JOIN pais p              ON p.pais_id        = l.pais_id
            LEFT JOIN ciudad c       ON c.ciudad_id      = l.ciudad_id
            WHERE l.pais_id = :paisId
            ORDER BY l.nombre
            """, nativeQuery = true)
    List<LugarResumenRow> listarPorPais(@Param("paisId") Long paisId);

    @Query(value = """
            SELECT
              l.lugar_id AS lugarId,
              p.nombre   AS pais,
              c.nombre   AS ciudad,
              cat.codigo AS categoriaCodigo,
              cat.nombre AS categoria,
              l.nombre   AS lugar,
              l.direccion AS direccion,
              l.precio   AS precio,
              l.promedio_estrellas AS promedioEstrellas,
              (SELECT url FROM imagen_lugar i
                 WHERE i.lugar_id = l.lugar_id
                 FETCH FIRST 1 ROWS ONLY) AS imagenPrincipal
            FROM lugar l
            JOIN categoria_lugar cat ON cat.categoria_id = l.categoria_id
            JOIN pais p              ON p.pais_id        = l.pais_id
            LEFT JOIN ciudad c       ON c.ciudad_id      = l.ciudad_id
            WHERE l.pais_id = :paisId
              AND (:ciudadId IS NULL OR l.ciudad_id = :ciudadId)
              AND (:categoriaCodigo IS NULL OR UPPER(cat.codigo) = UPPER(:categoriaCodigo))
              AND (:minEstrellas IS NULL OR l.promedio_estrellas >= :minEstrellas)
              AND (:maxPrecio    IS NULL OR l.precio <= :maxPrecio)
              AND (:nombre IS NULL OR LOWER(l.nombre) LIKE LOWER('%' || :nombre || '%'))
            ORDER BY l.nombre
            """, nativeQuery = true)
    List<LugarResumenRow> buscarFiltrado(@Param("paisId") Long paisId,
                                         @Param("ciudadId") Long ciudadId,
                                         @Param("categoriaCodigo") String categoriaCodigo,
                                         @Param("minEstrellas") BigDecimal minEstrellas,
                                         @Param("maxPrecio") BigDecimal maxPrecio,
                                         @Param("nombre") String nombre);
}
