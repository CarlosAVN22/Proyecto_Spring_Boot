package com.example.demo.web;

import com.example.demo.catalogo.ImagenLugarRepository;
import com.example.demo.catalogo.LugarDetalleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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

        // Imágenes
        List<String> imgs = imagenRepo.listarUrls(id);
        if (imgs == null) imgs = List.of();
        String imagenPrincipal = imgs.isEmpty() ? null : imgs.get(0);

        // Null-safe helpers
        String titulo     = firstNonBlank(get(d, "nombre"), get(d, "lugar"), "Lugar");
        String categoria  = firstNonBlank(get(d, "categoria"), get(d, "categoriaNombre"), "Categoría");
        String ciudad     = firstNonBlank(get(d, "ciudad"), get(d, "ciudadNombre"), "Ciudad");
        String pais       = firstNonBlank(get(d, "pais"), get(d, "paisNombre"), "País");
        String direccion  = firstNonBlank(get(d, "direccion"), "—");
        String catCodigo  = firstNonBlank(get(d, "categoriaCodigo"), "—");

        // estrellas/precio (null-safe)
        Number estrellasNum = getNumber(d, "promedioEstrellas", 0);
        double rating = estrellasNum == null ? 0.0 : estrellasNum.doubleValue();
        if (rating < 0) rating = 0;
        if (rating > 5) rating = 5;
        int starPercent = (int) Math.round(rating * 20);                 // 0..100
        String ratingText = String.format(Locale.US, "%.1f", rating);    // "4.3"

        Number precioNum  = getNumber(d, "precio", null);
        String precioFmt  = (precioNum == null) ? "—"
                : "$" + NumberFormat.getNumberInstance(new Locale("es", "MX"))
                                    .format(precioNum.doubleValue());

        var vm = new LugarVM(
                titulo, categoria, ciudad, pais,
                direccion, precioFmt, ratingText, starPercent, catCodigo
        );

        model.addAttribute("vm", vm);
        model.addAttribute("imagenes", imgs);
        model.addAttribute("imagenPrincipal", imagenPrincipal);
        return "lugar-detalle";
    }

    // ---- ViewModel simple para la vista ----
    public record LugarVM(
            String titulo,
            String categoria,
            String ciudad,
            String pais,
            String direccion,
            String precio,
            String ratingText,   // "4.3"
            int starPercent,     // 0..100
            String categoriaCodigo
    ) {}

    // ---- Utilidades null-safe ----
    private static String get(Object o, String prop){
        try {
            var m = o.getClass().getMethod("get" + capitalize(prop));
            Object v = m.invoke(o);
            return v == null ? null : String.valueOf(v);
        } catch (Exception e) {
            return null;
        }
    }
    private static Number getNumber(Object o, String prop, Number def){
        try {
            var m = o.getClass().getMethod("get" + capitalize(prop));
            Object v = m.invoke(o);
            if (v instanceof Number n) return n;
            if (v != null) return Double.parseDouble(v.toString());
            return def;
        } catch (Exception e) {
            return def;
        }
    }
    private static String firstNonBlank(String... vals){
        for(String s: vals){
            if(s != null && !s.isBlank()) return s;
        }
        return "";
    }
    private static String capitalize(String s){
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
