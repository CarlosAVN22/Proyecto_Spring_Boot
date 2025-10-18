package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/explorar")
public class ExploracionController {

    @GetMapping("/general")
    public String general(@RequestParam Long paisId, Model model) {
        model.addAttribute("paisId", paisId);
        return "explorar-general";
    }

    @GetMapping("/especifico")
    public String especifico(@RequestParam Long paisId, Model model) {
        model.addAttribute("paisId", paisId);
        return "explorar-especifico";
    }
}
