package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import org.springframework.data.repository.CrudRepository;

public interface VideoRepository extends CrudRepository<Video, Long> {
}
