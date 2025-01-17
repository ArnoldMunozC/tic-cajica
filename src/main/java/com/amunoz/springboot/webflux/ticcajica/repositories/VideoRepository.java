package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface VideoRepository extends CrudRepository<Video, Long> {

    @Query("select v FROM Video v WHERE v.cursoId = ?1")
    List<Video> findByCursoId(UUID id);
}
