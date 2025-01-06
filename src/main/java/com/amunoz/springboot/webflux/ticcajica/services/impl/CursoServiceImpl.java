package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.repositories.CursoRepository;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Override
    public void save(Curso curso) {
        cursoRepository.save(curso);
    }

    @Override
    public Curso findById(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        cursoRepository.deleteById(id);
    }


    @Override
    public Curso update(Curso curse, Long id) {
        return cursoRepository.save(curse);
    }

    @Override
    public Curso findByNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }


}
