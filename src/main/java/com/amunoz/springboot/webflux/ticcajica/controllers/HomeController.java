package com.amunoz.springboot.webflux.ticcajica.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/",""})
    public String home() {
        // Devuelve el nombre del archivo HTML que actuará como la página principal
        return "index"; // Busca un archivo 'index.html' en la carpeta 'templates'
    }
}
