package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoService {

    List<Video> findByCursoId(UUID id);

    void save (Video course);

    Video findById(Long id);

    void delete(Long id);

    Video update(Video course, Long id);
}
