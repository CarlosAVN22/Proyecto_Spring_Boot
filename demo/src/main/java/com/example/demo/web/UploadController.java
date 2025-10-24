package com.example.demo.web;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final Path root = Paths.get("uploads"); // carpeta local

    public UploadController() throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Archivo vacío"));

        var ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (ext == null) ext = "bin";
        var name = UUID.randomUUID() + "." + ext.toLowerCase();
        var dest = root.resolve(name);

        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        // Sirve estático desde /uploads (ver paso 2)
        var url = "/uploads/" + name;
        return ResponseEntity.ok(Map.of("ok", true, "url", url));
    }
}
