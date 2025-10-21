package com.example.demo.web;

import com.example.demo.catalogo.CiudadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadesApiController {

    private final CiudadRepository ciudades;

    public CiudadesApiController(CiudadRepository ciudades) {
        this.ciudades = ciudades;
    }

    @GetMapping
    public ResponseEntity<?> listarPorPais(@RequestParam Long paisId) {
        return ResponseEntity.ok(ciudades.findByPaisIdOrderByNombreAsc(paisId));
    }
}
