package com.example.demo.web;

import com.example.demo.catalogo.PaisRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SeleccionController {

    private final PaisRepository paises;

    public SeleccionController(PaisRepository paises) {
        this.paises = paises;
    }

    @GetMapping("/pais/{id}")
    public String redirigirPorId(@PathVariable Long id) {
        paises.findById(id).orElseThrow(() -> new IllegalArgumentException("País no encontrado: " + id));
        return "redirect:/explorar/general?paisId=" + id;
    }
}
