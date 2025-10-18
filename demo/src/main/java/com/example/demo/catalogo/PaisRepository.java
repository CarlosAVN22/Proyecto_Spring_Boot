package com.example.demo.catalogo;

import org.springframework.data.repository.ListCrudRepository;
import java.util.List;
import java.util.Optional;

public interface PaisRepository extends ListCrudRepository<Pais, Long> {
    List<Pais> findAllByOrderByNombreAsc();
    Optional<Pais> findByCodigoPaisIgnoreCase(String codigoPais);
}
