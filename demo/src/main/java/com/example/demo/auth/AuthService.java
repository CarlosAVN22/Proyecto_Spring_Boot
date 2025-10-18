package com.example.demo.auth;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.RegisterRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarios, RolRepository roles) {
        this.usuarios = usuarios;
        this.roles = roles;
    }

    public Usuario register(RegisterRequest r) {
        if (usuarios.existsByEmail(r.email().toLowerCase())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (usuarios.existsByDui(r.dui())) {
            throw new IllegalArgumentException("El DUI ya está registrado");
        }

        Usuario u = new Usuario();
        u.setNombre(r.nombre());
        u.setApellido(r.apellido());
        u.setEmail(r.email().toLowerCase());
        u.setContrasenaHash(encoder.encode(r.password()));
        u.setDui(r.dui());
        u.setTelefono(r.telefono());

        // Asegura que el rol 'USUARIO' exista y asígnalo
        Rol rolUsuario = roles.findByCodigo("USUARIO")
                .orElseThrow(() -> new IllegalStateException("Falta rol 'USUARIO' en tabla ROL"));
        u.getRoles().add(rolUsuario);

        return usuarios.save(u);
    }

    public Usuario login(LoginRequest r) {
        Usuario u = usuarios.findByEmail(r.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña inválidos"));
        if (!encoder.matches(r.password(), u.getContrasenaHash())) {
            throw new IllegalArgumentException("Usuario o contraseña inválidos");
        }
        return u;
    }
}
