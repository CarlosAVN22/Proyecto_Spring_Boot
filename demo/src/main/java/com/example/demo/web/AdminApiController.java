package com.example.demo.web;

import com.example.demo.catalogo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminApiController {

    private final LugarRepository lugares;
    private final CategoriaLugarRepository categorias;
    private final PaisRepository paises;
    private final ImagenLugarRepository imagenes;

    public AdminApiController(
            LugarRepository lugares,
            CategoriaLugarRepository categorias,
            PaisRepository paises,
            ImagenLugarRepository imagenes
    ) {
        this.lugares = lugares;
        this.categorias = categorias;
        this.paises = paises;
        this.imagenes = imagenes;
    }

    // ---------- Catálogo categorías ----------
    @GetMapping("/categorias")
    public List<CategoriaDto> categorias() {
        return categorias.findAll().stream()
                .map(c -> new CategoriaDto(c.getId(), c.getCodigo(), c.getNombre()))
                .toList();
    }

    // ---------- Buscar / Listar ----------
    @GetMapping("/lugares")
    public List<LugarDto> listar(
            @RequestParam(required = false) Long paisId,
            @RequestParam(required = false, defaultValue = "") String q
    ) {
        List<Lugar> res;
        if (paisId != null && !q.isBlank()) {
            res = lugares.findByPaisIdAndNombreContainingIgnoreCase(paisId, q.trim());
        } else if (paisId != null) {
            res = lugares.findByPaisIdOrderByNombreAsc(paisId);
        } else if (!q.isBlank()) {
            res = lugares.findByNombreContainingIgnoreCase(q.trim());
        } else {
            res = lugares.findTop50ByOrderByIdDesc();
        }
        return res.stream().map(this::mapLugar).toList();
    }

    @GetMapping("/lugares/{id}")
    public ResponseEntity<LugarDto> ver(@PathVariable Long id) {
        return lugares.findById(id)
                .map(l -> ResponseEntity.ok(mapLugar(l)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- Crear ----------
    @PostMapping("/admin/lugares")
    @Transactional
    public ResponseEntity<?> crear(@RequestBody CrearActualizarLugar r) {
        var pais = requirePais(r.paisId());
        var cat  = requireCategoria(r.categoriaId(), r.categoriaCodigo());

        var l = new Lugar();
        l.setNombre(nonNullTrim(r.nombre(), "nombre"));
        l.setPrecio(asBigDecimal(r.precio()));              // Number -> BigDecimal
        l.setDireccion(emptyToNull(r.direccion()));
        l.setPais(pais);
        l.setCategoria(cat);
        lugares.save(l);

        if (isHttpUrl(r.imagenUrl())) {
            var img = new ImagenLugar();
            img.setLugar(l);
            img.setUrl(r.imagenUrl().trim());
            imagenes.save(img);
        }
        return ResponseEntity.ok(mapLugar(l));
    }

    // ---------- Editar ----------
    @PutMapping("/admin/lugares/{id}")
    @Transactional
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody CrearActualizarLugar r) {
        var lOpt = lugares.findById(id);
        if (lOpt.isEmpty()) return ResponseEntity.notFound().build();

        var l = lOpt.get();

        if (r.paisId() != null) {
            l.setPais(requirePais(r.paisId()));
        }
        if (r.categoriaId() != null || r.categoriaCodigo() != null) {
            l.setCategoria(requireCategoria(r.categoriaId(), r.categoriaCodigo()));
        }
        if (r.nombre() != null) {
            l.setNombre(nonNullTrim(r.nombre(), "nombre"));
        }
        if (r.direccion() != null) {
            l.setDireccion(emptyToNull(r.direccion()));
        }
        if (r.precio() != null) {
            l.setPrecio(asBigDecimal(r.precio()));
        }
        lugares.save(l);

        if (r.imagenUrl() != null && isHttpUrl(r.imagenUrl())) {
            var first = imagenes.findFirstByLugarIdOrderByIdAsc(l.getId()).orElse(null);
            if (first == null) {
                first = new ImagenLugar();
                first.setLugar(l);
            }
            first.setUrl(r.imagenUrl().trim());
            imagenes.save(first);
        }

        return ResponseEntity.ok(mapLugar(l));
    }

    // ---------- Helpers ----------
    private LugarDto mapLugar(Lugar l) {
        String imagenUrl = imagenes.findFirstByLugarIdOrderByIdAsc(l.getId())
                .map(ImagenLugar::getUrl)
                .orElse(null);

        return new LugarDto(
                l.getId(),
                l.getNombre(),
                l.getPais().getNombre(), l.getPais().getId(),
                l.getCategoria().getNombre(), l.getCategoria().getId(),
                l.getDireccion(),
                null,
                imagenUrl,
                l.getPrecio()
        );
    }

    private boolean isHttpUrl(String s) {
        return s != null && s.matches("(?i)^https?://.+");
    }

    private BigDecimal asBigDecimal(Number n) {
        if (n == null) return BigDecimal.ZERO;
        return (n instanceof BigDecimal bd) ? bd : BigDecimal.valueOf(n.doubleValue());
    }

    private String emptyToNull(String s){
        return (s == null || s.trim().isEmpty()) ? null : s.trim();
    }

    private String nonNullTrim(String s, String field){
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Falta campo obligatorio: " + field);
        }
        return s.trim();
    }

    private Pais requirePais(Long id){
        return paises.findById(id).orElseThrow(() ->
                new IllegalArgumentException("País no encontrado: " + id));
    }

    private CategoriaLugar requireCategoria(Long id, String codigo){
        if (id != null) {
            return categorias.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("Categoría no encontrada (id): " + id));
        }
        if (codigo != null && !codigo.isBlank()) {
            // intenta ignoreCase; si no existe ese método, usará el tuyo
            return categorias.findByCodigoIgnoreCase(codigo.trim())
                    .or(() -> categorias.findByCodigo(codigo.trim()))
                    .orElseThrow(() ->
                            new IllegalArgumentException("Categoría no encontrada (codigo): " + codigo));
        }
        throw new IllegalArgumentException("Debes enviar categoriaId o categoriaCodigo");
    }

    // ---------- DTOs ----------
    public record CategoriaDto(Long id, String codigo, String nombre) {}

    // ACEPTA AMBOS: categoriaCodigo (lo que manda tu JS) o categoriaId
    public record CrearActualizarLugar(
            String nombre,
            Long paisId,
            Long categoriaId,
            String categoriaCodigo,
            String direccion,
            String descripcion,
            String imagenUrl,
            Number precio
    ) {}

    public record LugarDto(
            Long id,
            String nombre,
            String pais, Long paisId,
            String categoria, Long categoriaId,
            String direccion,
            String descripcion,
            String imagenUrl,
            BigDecimal precio
    ) {}
}
