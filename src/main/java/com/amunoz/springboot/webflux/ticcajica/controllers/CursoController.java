package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.constants.CursoConstants;
import com.amunoz.springboot.webflux.ticcajica.domain.ResponseDTO;
import com.amunoz.springboot.webflux.ticcajica.exception.CustomerAlreadyExistsException;
import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import com.amunoz.springboot.webflux.ticcajica.services.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/v1/curso")
public class CursoController {

    private final CursoService cursoService;


    public CursoController(CursoService cursoService, VideoService videoService) {
        this.cursoService = cursoService;
    }

    @PostMapping("/create-curso")
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

    }

    @PostMapping("/find-curso")
    public ResponseEntity<Curso> findCurso(@RequestBody Curso curso) {
        Curso curso1 = cursoService.findByNombre(curso.getNombre());

        if (curso1 == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(curso1);

    }

    @PutMapping("/update-curso/{id}")
    public ResponseEntity<ResponseDTO> updateCurso(@RequestBody Curso curso, @PathVariable Long id) {
        Curso curso1 = cursoService.update(curso, id);

        if (curso1 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(CursoConstants.STATUS_404, CursoConstants.MESSAGE_404));
        }
        return ResponseEntity.ok(new ResponseDTO(CursoConstants.STATUS_201, CursoConstants.MESSAGE_201));

    }

}
