package com.example.demo.web;

import com.example.demo.catalogo.CiudadRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoApiController {

    private final CiudadRepository ciudades;

    public CatalogoApiController(CiudadRepository ciudades) {
        this.ciudades = ciudades;
    }

    @GetMapping("/ciudades")
    public ResponseEntity<?> ciudades(@RequestParam Long paisId){
        return ResponseEntity.ok(ciudades.findByPaisIdOrderByNombreAsc(paisId));
    }
}
