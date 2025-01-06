package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoService {

    void save (Video course);

    Video findById(Long id);

    void delete(Long id);

    Video update(Video course, Long id);
}
