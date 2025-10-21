package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/admin")
    public String adminPanel() {
        // retorna el archivo admin-panel.html de templates
        return "admin-panel";
    }

    @GetMapping("/admin/lugar")
    public String adminLugarForm() {
        // si tienes otro HTML como admin-lugar-form.html
        return "admin-lugar-form";
    }
}
