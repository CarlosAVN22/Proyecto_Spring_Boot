package com.example.demo.web;

import com.example.demo.catalogo.Pais;
import com.example.demo.catalogo.PaisRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class PaisesController {

    private final PaisRepository paises;

    public PaisesController(PaisRepository paises) {
        this.paises = paises;
    }

    // Mapa simple: código (3 letras) -> emoji bandera
    private static final Map<String, String> FLAG = Map.ofEntries(
        Map.entry("SLV", "🇸🇻"),
        Map.entry("GTM", "🇬🇹"),
        Map.entry("HND", "🇭🇳"),
        Map.entry("NIC", "🇳🇮"),
        Map.entry("CRI", "🇨🇷"),
        Map.entry("PAN", "🇵🇦"),
        Map.entry("BLZ", "🇧🇿")
    );

    @GetMapping("/paises")
    public String vistaPaises(Model model) {
        var lista = paises.findAllByOrderByNombreAsc();
        List<Map<String,Object>> items = new ArrayList<>();
        for (Pais p : lista) {
            items.add(Map.of(
                "id", p.getId(),
                "nombre", p.getNombre(),
                "codigo", p.getCodigoPais(),
                "flag", FLAG.getOrDefault(p.getCodigoPais(), "🏳️")
            ));
        }
        model.addAttribute("paises", items);
        return "paises";
    }

    // API opcional (útil para debug)
    @GetMapping("/api/paises")
    public ResponseEntity<?> apiPaises() {
        return ResponseEntity.ok(paises.findAllByOrderByNombreAsc());
    }
}
