package com.amunoz.springboot.webflux.ticcajica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapeo virtual de la ruta en el navegador a la ruta de tu PC
        registry.addResourceHandler("/videos/**") // URL accesible desde el navegador
                .addResourceLocations("file:/C:/Users/Arnold Mauricio/Documents/videos/"); // Ruta del sistema de archivos
    }
}
