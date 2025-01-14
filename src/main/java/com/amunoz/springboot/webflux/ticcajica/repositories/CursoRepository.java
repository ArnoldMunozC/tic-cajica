package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CursoRepository extends CrudRepository<Curso, UUID> {

    @Query("SELECT u FROM Curso u WHERE u.nombre = ?1")
    Curso findByNombre(String nombre);


    @Query("SELECT u FROM Curso u WHERE u.nombre like %?1%")
    List<Curso> findByContainNombre(String term);


    List<Curso> findAll();




}
