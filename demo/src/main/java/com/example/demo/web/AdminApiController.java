package com.example.demo.web;

import com.example.demo.admin.dto.CreateLugarRequest;
import com.example.demo.catalogo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private final LugarRepository lugares;
    private final CategoriaLugarRepository categorias;
    private final PaisRepository paises;
    private final CiudadRepository ciudades;
    private final ImagenLugarRepository imagenes;

    public AdminApiController(
            LugarRepository lugares,
            CategoriaLugarRepository categorias,
            PaisRepository paises,
            CiudadRepository ciudades,
            ImagenLugarRepository imagenes
    ) {
        this.lugares = lugares;
        this.categorias = categorias;
        this.paises = paises;
        this.ciudades = ciudades;
        this.imagenes = imagenes;
    }

    @PostMapping("/lugares")
    public ResponseEntity<?> crearLugar(@RequestBody CreateLugarRequest r) {

        // ===== VALIDACIONES AMIGABLES (evitan 500 por NPE/IllegalArgument de JPA) =====
        if (r == null) throw new IllegalArgumentException("Payload vacío.");
        if (r.nombre() == null || r.nombre().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (r.categoriaCodigo() == null || r.categoriaCodigo().isBlank())
            throw new IllegalArgumentException("La categoría es obligatoria.");
        if (r.paisId() == null)
            throw new IllegalArgumentException("Debes seleccionar un país.");

        // ===== BUSCAR CATEGORÍA/PÁIS/CIUDAD =====
        var cat = categorias.findByCodigo(r.categoriaCodigo().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + r.categoriaCodigo()));

        var pais = paises.findById(r.paisId())
                .orElseThrow(() -> new IllegalArgumentException("País no encontrado: " + r.paisId()));

        var ciudad = (r.ciudadId() == null) ? null :
                ciudades.findById(r.ciudadId())
                        .orElseThrow(() -> new IllegalArgumentException("Ciudad no encontrada: " + r.ciudadId()));

        if (ciudad != null && !ciudad.getPais().getId().equals(pais.getId())) {
            throw new IllegalArgumentException("La ciudad no pertenece al país seleccionado.");
        }

        // ===== CREAR / GUARDAR LUGAR =====
        var l = new Lugar();
        l.setNombre(trim(r.nombre()));
        l.setCategoria(cat);
        l.setPais(pais);
        l.setCiudad(ciudad);
        l.setDireccion(trim(r.direccion()));
        if (r.precio() != null) l.setPrecio(toBigDecimal(r.precio()));

        var saved = lugares.save(l);

        // ===== IMAGEN OPCIONAL =====
        if (r.imagenUrl() != null && !r.imagenUrl().isBlank()) {
            var img = new ImagenLugar();
            img.setLugar(saved);
            img.setUrl(r.imagenUrl().trim());
            imagenes.save(img);
        }

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "id", saved.getId(),
                "message", "Lugar creado"
        ));
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static BigDecimal toBigDecimal(Number n) {
        if (n instanceof BigDecimal bd) return bd;
        return new BigDecimal(String.valueOf(n));
    }
}
