package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.constants.CursoConstants;
import com.amunoz.springboot.webflux.ticcajica.domain.ResponseDTO;
import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import com.amunoz.springboot.webflux.ticcajica.services.VideoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/curso")
public class CursoController {

    private final CursoService cursoService;
    private final VideoService videoService;


    public CursoController(CursoService cursoService, VideoService videoService) {
        this.cursoService = cursoService;
        this.videoService = videoService;
    }



/*    @PostMapping("/create-curso")
    public ResponseEntity<ResponseDTO> createCurso(@RequestBody Curso curso) {
        Optional<Curso> optionalCurso = Optional.ofNullable(cursoService.findByNombre(curso.getNombre()));

        if (curso == null) {
            return ResponseEntity.badRequest().build();
        }
        if (optionalCurso.isPresent()) {
            throw new CustomerAlreadyExistsException("Curso already exists with given name " + curso.getNombre());
        }

        cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(CursoConstants.STATUS_201, CursoConstants.MESSAGE_201));

    }*/

    @PostMapping("/create-curso")
    public String createCurso(Curso curso, @RequestParam MultipartFile archivo) throws IOException {

        if (!archivo.isEmpty()) {
            curso.setImagen(archivo.getBytes());
        }

        cursoService.save(curso);
        return "redirect:/api/v1/curso/all-cursos";


    }

    @GetMapping("/form-create-curso")
    public String showFormCreateCurso() {
        return "form-create-curso";
    }

    @GetMapping("/all-cursos")
    public String getAllCursos(Model model) {
        List<Curso> cursos = cursoService.findAll();
        if (cursos.isEmpty()) {
            model.addAttribute("mensaje", "No hay cursos disponibles");
        } else {
            model.addAttribute("cursos", cursos);
        }
        return "cursos"; // Nombre de la vista (por ejemplo, cursos.html en templates)
    }

    @PostMapping("/find-curso/{nombre}")
    public ResponseEntity<List<Curso>> findCurso(@PathVariable String nombre) {
        List<Curso> cursos = cursoService.findCursoByNombre(nombre);

        if (cursos.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cursos);

    }


/*    @PutMapping("/update-curso/{id}")
    public ResponseEntity<ResponseDTO> updateCurso(@RequestBody Curso curso, @PathVariable UUID id) {
        Curso curso1 = cursoService.update(curso, id);

        if (curso1 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(CursoConstants.STATUS_404, CursoConstants.MESSAGE_404));
        }
        return ResponseEntity.ok(new ResponseDTO(CursoConstants.STATUS_201, CursoConstants.MESSAGE_201));

    }*/

    @PutMapping("/update-curso/{id}")
    public ResponseEntity<ResponseDTO> updateCurso(Curso curso, @PathVariable UUID id, @RequestParam MultipartFile archivo) throws IOException {
        Curso curso1 = cursoService.update(curso, id, archivo);

        if (curso1 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(CursoConstants.STATUS_404, CursoConstants.MESSAGE_404));
        }
        return ResponseEntity.ok(new ResponseDTO(CursoConstants.STATUS_201, CursoConstants.MESSAGE_201));

    }

    // muestra la informacion del curso incluyebdo los videos
    @GetMapping("/byId/{id}")
    public String getCursoById(@PathVariable UUID id, Model model) {
        try {
            Curso curso = cursoService.findById(id);

            if (curso == null) {
                model.addAttribute("error", "Curso no encontrado");
                return "error"; // Nombre del template para errores personalizados
            }

            List<Video> videos = videoService.findByCursoId(id);
            curso.setVideos(videos);
            model.addAttribute("curso", curso);
            model.addAttribute("videos", videos);

            return "detalleCurso"; // Nombre de la plantilla de Thymeleaf
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error inesperado");
            return "error"; // Nombre del template personalizado para cualquier error
        }
    }

    @GetMapping("upload/img/{id}")
    public ResponseEntity<?> verImagen(@PathVariable UUID id) {
        Curso curso = cursoService.findById(id);
        if (curso == null || curso.getImagen() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Imagen no encontrada");
        }
        Resource imagen = new ByteArrayResource(curso.getImagen());

        // Detectar tipo de archivo, asumo que usas JPEG aquí
        MediaType mediaType = MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imagen);
    }

}
