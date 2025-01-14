package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
public interface CursoService {

    void save (Curso curso);

    Curso findById(UUID id);

    void delete(UUID id);

    Curso update(Curso course, UUID id, MultipartFile file) throws IOException;

    Curso findByNombre(String nombre);

    List<Curso> findCursoByNombre(String nombre);

    List<Curso> findAll();




}
