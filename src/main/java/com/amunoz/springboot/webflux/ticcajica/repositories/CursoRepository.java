package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.Curso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends CrudRepository<Curso, Long> {

    @Query("SELECT u FROM Curso u WHERE u.nombre = ?1")
    Curso findByNombre(String nombre);
}
