package com.example.demo.catalogo;

import com.example.demo.web.dto.LugarDetalleDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repositorio de solo-lectura para obtener el detalle armado de un Lugar.
 * (Puedes extender JpaRepository<Lugar, Long> si prefieres, pero así evitamos
 * solapar con tu LugarRepository existente.)
 */
public interface LugarDetalleRepository extends Repository<Lugar, Long> {

    @Query("""
        select new com.example.demo.web.dto.LugarDetalleDTO(
            l.id, l.nombre,
            p.id, p.nombre,
            c.id, c.nombre,
            l.direccion, l.precio
        )
        from Lugar l
        join l.pais p
        join l.categoria c
        where l.id = :id
    """)
    Optional<LugarDetalleDTO> obtenerDetalle(@Param("id") Long id);
}
