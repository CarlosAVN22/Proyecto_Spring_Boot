package com.example.demo.dev;

import com.example.demo.auth.Rol;
import com.example.demo.auth.RolRepository;
import com.example.demo.auth.Usuario;
import com.example.demo.auth.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dev")
public class DevAdminController {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public DevAdminController(UsuarioRepository usuarios, RolRepository roles) {
        this.usuarios = usuarios;
        this.roles = roles;
    }

    public record UpsertAdminReq(String email, String password) {}

    private Rol ensureRol(String codigo, String nombre) {
        return roles.findByCodigo(codigo).orElseGet(() -> {
            Rol r = new Rol();
            r.setCodigo(codigo);
            r.setNombre(nombre);
            return roles.save(r);
        });
    }

    @PostMapping("/upsert-admin")
    public ResponseEntity<?> upsertAdmin(@RequestBody UpsertAdminReq body) {
        try {
            final String email = body.email().trim().toLowerCase();
            final String rawPass = body.password();

            // Asegurar roles
            Rol rolUsuario = ensureRol("USUARIO", "Usuario");
            Rol rolAdmin   = ensureRol("ADMIN",   "Administrador");

            var opt = usuarios.findByEmail(email);
            if (opt.isPresent()) {
                // Update password
                Usuario u = opt.get();
                u.setContrasenaHash(encoder.encode(rawPass));
                // Asegurar que tenga ADMIN
                u.getRoles().add(rolUsuario);
                u.getRoles().add(rolAdmin);
                usuarios.save(u);
                return ResponseEntity.ok(Map.of("ok", true, "message", "Password reseteada y roles asegurados", "id", u.getId(), "email", u.getEmail()));
            }

            // Crear nuevo admin
            Usuario u = new Usuario();
            u.setNombre("Admin");
            u.setApellido("Tourify");
            u.setEmail(email);
            u.setContrasenaHash(encoder.encode(rawPass));

            // DUI único
            String dui = "00000000-0";
            int intento = 0;
            while (usuarios.existsByDui(dui)) {
                intento++;
                dui = ("0000000" + (System.currentTimeMillis() % 10_000_000)) + "-" + (intento % 10);
            }
            u.setDui(dui);
            u.setTelefono("7000-0000");

            u.getRoles().add(rolUsuario);
            u.getRoles().add(rolAdmin);
            usuarios.save(u);

            return ResponseEntity.ok(Map.of("ok", true, "message", "Admin creado", "id", u.getId(), "email", u.getEmail(), "dui", u.getDui()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("ok", false, "message", e.getMessage()));
        }
    }

    // Debug: ver info del usuario
    @GetMapping("/user")
    public ResponseEntity<?> userInfo(@RequestParam String email) {
        var u = usuarios.findByEmail(email.trim().toLowerCase()).orElse(null);
        if (u == null) return ResponseEntity.ok(Map.of("exists", false));
        return ResponseEntity.ok(Map.of(
                "exists", true,
                "id", u.getId(),
                "email", u.getEmail(),
                "dui", u.getDui(),
                "roles", u.getRoles().stream().map(Rol::getCodigo).toList()
        ));
    }

    // Debug: probar match de contraseña sin pasar por el controlador de auth
    public record TestReq(String email, String password) {}
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestBody TestReq body) {
        var u = usuarios.findByEmail(body.email().trim().toLowerCase()).orElse(null);
        if (u == null) return ResponseEntity.ok(Map.of("exists", false));
        boolean matches = encoder.matches(body.password(), u.getContrasenaHash());
        return ResponseEntity.ok(Map.of("exists", true, "matches", matches, "id", u.getId(), "email", u.getEmail()));
    }
}
