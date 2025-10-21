package com.example.demo.web;

import com.example.demo.catalogo.CategoriaLugarRepository;
import com.example.demo.catalogo.CiudadRepository;
import com.example.demo.catalogo.PaisRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/explorar")
public class ExploracionController {

    private final PaisRepository paises;
    private final CategoriaLugarRepository categorias;
    private final CiudadRepository ciudades;

    public ExploracionController(PaisRepository paises,
                                 CategoriaLugarRepository categorias,
                                 CiudadRepository ciudades) {
        this.paises = paises;
        this.categorias = categorias;
        this.ciudades = ciudades;
    }

    @GetMapping("/general")
    public String general(@RequestParam Long paisId, Model model) {
        var pais = paises.findById(paisId)
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado: " + paisId));
        model.addAttribute("paisId", pais.getId());
        model.addAttribute("pais", pais);
        model.addAttribute("categorias", categorias.findAll());
        model.addAttribute("ciudades", ciudades.findByPaisIdOrderByNombreAsc(pais.getId()));
        return "explorar-general";
    }

    @GetMapping("/especifico")
    public String especifico(@RequestParam Long paisId, Model model) {
        var pais = paises.findById(paisId)
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado: " + paisId));
        model.addAttribute("paisId", pais.getId());
        model.addAttribute("pais", pais);
        model.addAttribute("categorias", categorias.findAll()); // por si luego generas tiles dinámicos
        model.addAttribute("ciudades", ciudades.findByPaisIdOrderByNombreAsc(pais.getId())); // no usamos dropdown aquí
        return "explorar-especifico";
    }
}
