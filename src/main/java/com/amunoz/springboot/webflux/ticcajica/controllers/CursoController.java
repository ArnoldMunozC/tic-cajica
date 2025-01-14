package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.constants.CursoConstants;
import com.amunoz.springboot.webflux.ticcajica.domain.ResponseDTO;
import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin({"http://localhost:4200"})
@RestController
@RequestMapping("/api/v1/curso")
public class CursoController {

    private final CursoService cursoService;


    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
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
    public ResponseEntity<ResponseDTO> createCurso(Curso curso, @RequestParam MultipartFile archivo) throws IOException {

        if (!archivo.isEmpty()) {
            curso.setImagen(archivo.getBytes());
        }

        cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(CursoConstants.STATUS_201, CursoConstants.MESSAGE_201));

    }

    @GetMapping("/all-cursos")
    public ResponseEntity<List<Curso>> getAllCursos() {
        List<Curso> cursos = cursoService.findAll();
        if (cursos.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cursos);
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

    @GetMapping("/byId/{id}")
    public ResponseEntity<Curso> getCursoById(@PathVariable UUID id) {
        Curso curso = cursoService.findById(id);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/upload/img/{id}")
    public ResponseEntity<?> verImagen(@PathVariable UUID id) {
        Curso curso = cursoService.findById(id);
        if (curso == null || curso.getImagen() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Resource imagen = new ByteArrayResource(curso.getImagen());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
    }

}
