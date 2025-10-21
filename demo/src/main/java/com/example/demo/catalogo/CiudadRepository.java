package com.example.demo.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    @Query("select c from Ciudad c where c.pais.id = :paisId order by c.nombre asc")
    List<Ciudad> findByPaisIdOrderByNombreAsc(@Param("paisId") Long paisId);
}
