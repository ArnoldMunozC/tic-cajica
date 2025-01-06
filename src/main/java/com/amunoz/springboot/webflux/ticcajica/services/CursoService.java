package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import org.springframework.stereotype.Repository;

import java.util.Random;

@Repository
public interface CursoService {

    void save (Curso curso);

    Curso findById(Long id);

    void delete(Long id);

    Curso update(Curso course, Long id);

    Curso findByNombre(String nombre);



}
