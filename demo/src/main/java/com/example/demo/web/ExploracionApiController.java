package com.example.demo.web;

import com.example.demo.catalogo.LugarResumenRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/explorar")
public class ExploracionApiController {

    private final LugarResumenRepository lugares;

    public ExploracionApiController(LugarResumenRepository lugares) {
        this.lugares = lugares;
    }

    // GENERAL: todas las opciones del país
    @GetMapping("/general")
    public ResponseEntity<?> general(@RequestParam Long paisId) {
        return ResponseEntity.ok(lugares.listarPorPais(paisId));
    }

    // ESPECÍFICO: búsqueda por filtros (todos opcionales excepto paisId)
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam Long paisId,
                                    @RequestParam(required = false) Long ciudadId,
                                    @RequestParam(required = false) String categoriaCodigo,
                                    @RequestParam(required = false) BigDecimal minEstrellas,
                                    @RequestParam(required = false) BigDecimal maxPrecio,
                                    @RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(
                lugares.buscarFiltrado(paisId, ciudadId, categoriaCodigo, minEstrellas, maxPrecio, nombre)
        );
    }
}
