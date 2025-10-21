package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest body) {
        try {
            var u = auth.register(body);
            return ResponseEntity.ok(Map.of(
                "ok", true,
                "id", u.getId(),
                "nombre", u.getNombre(),
                "email", u.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body(Map.of("ok", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body, HttpSession session) {
        try {
            var u = auth.login(body);
            // Guarda info mínima en sesión
            session.setAttribute("UID", u.getId());
            session.setAttribute("EMAIL", u.getEmail());
            session.setAttribute("NOMBRE", u.getNombre());
            session.setAttribute("ROLES", u.getRoles().stream().map(Rol::getCodigo).toList());

            return ResponseEntity.ok(Map.of(
                "ok", true,
                "id", u.getId(),
                "nombre", u.getNombre(),
                "email", u.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("ok", false, "message", e.getMessage()));
        }
    }

    // 👇 Nuevo: quién soy (lee desde sesión)
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        var uid = session.getAttribute("UID");
        if (uid == null) {
            return ResponseEntity.ok(Map.of("authenticated", false));
        }
        return ResponseEntity.ok(Map.of(
            "authenticated", true,
            "id", session.getAttribute("UID"),
            "email", session.getAttribute("EMAIL"),
            "nombre", session.getAttribute("NOMBRE"),
            "roles", session.getAttribute("ROLES")
        ));
    }

    // Opcional: logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
