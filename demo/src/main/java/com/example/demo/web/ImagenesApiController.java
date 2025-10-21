package com.example.demo.web;

import com.example.demo.catalogo.ImagenLugarRepository;
import com.example.demo.web.dto.ImagenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImagenesApiController {

    private final ImagenLugarRepository imagenes;

    public ImagenesApiController(ImagenLugarRepository imagenes) {
        this.imagenes = imagenes;
    }

    // /api/imagenes?lugarId=65
    @GetMapping("/imagenes")
    public ResponseEntity<List<String>> imagenesQuery(@RequestParam Long lugarId){
        return ResponseEntity.ok(imagenes.listarUrls(lugarId));
    }

    // /api/lugares/{id}/imagenes
    @GetMapping("/lugares/{id}/imagenes")
    public ResponseEntity<List<String>> imagenesPath(@PathVariable Long id){
        return ResponseEntity.ok(imagenes.listarUrls(id));
    }

    // Compatibilidad con tu panel (a veces llama /api/admin/imagenes?lugarId=...)
    @GetMapping("/admin/imagenes")
    public ResponseEntity<List<String>> imagenesAdmin(@RequestParam Long lugarId){
        return ResponseEntity.ok(imagenes.listarUrls(lugarId));
    }
}
