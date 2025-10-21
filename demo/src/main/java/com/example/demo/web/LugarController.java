package com.example.demo.web;

import com.example.demo.catalogo.LugarDetalleRepository;
import com.example.demo.catalogo.ImagenLugarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LugarController {

    private final LugarDetalleRepository detalleRepo;
    private final ImagenLugarRepository imagenRepo;

    public LugarController(LugarDetalleRepository detalleRepo, ImagenLugarRepository imagenRepo) {
        this.detalleRepo = detalleRepo;
        this.imagenRepo = imagenRepo;
    }

    @GetMapping("/lugar/{id}")
    public String detalle(@PathVariable Long id, Model model){
        var d = detalleRepo.obtenerDetalle(id)
                .orElseThrow(() -> new IllegalArgumentException("Lugar no encontrado: " + id));
        var imgs = imagenRepo.listarUrls(id);
        model.addAttribute("lugar", d);
        model.addAttribute("imagenes", imgs);
        return "lugar-detalle";
    }
}
