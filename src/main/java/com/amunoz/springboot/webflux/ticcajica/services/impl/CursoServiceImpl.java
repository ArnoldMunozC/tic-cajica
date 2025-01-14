package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import com.amunoz.springboot.webflux.ticcajica.repositories.CursoRepository;
import com.amunoz.springboot.webflux.ticcajica.services.CursoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Transactional
    @Override
    public void save(Curso curso) {
        cursoRepository.save(curso);
    }

    @Override
    public Curso findById(UUID id) {
        return cursoRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        cursoRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Curso update(Curso curse, UUID id, MultipartFile file) throws IOException {
        // Buscar el curso existente
        Curso cursoExistente = cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado con ID: " + id));

        // Actualizar los campos necesarios (personalizar esto según el caso)
        cursoExistente.setNombre(curse.getNombre());
        cursoExistente.setDescripcion(curse.getDescripcion());
        if (file != null) {
            cursoExistente.setImagen(file.getBytes());
        }

        // Guardar y devolver el curso actualizado
        return cursoRepository.save(cursoExistente);
    }


    @Override
    public Curso findByNombre(String nombre) {
        return cursoRepository.findByNombre(nombre);
    }

    @Override
    public List<Curso> findCursoByNombre(String nombre) {
        return cursoRepository.findByContainNombre(nombre);
    }

    @Override
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }


}
