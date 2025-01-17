package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.repositories.VideoRepository;
import com.amunoz.springboot.webflux.ticcajica.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;


    @Transactional(readOnly = true)
    @Override
    public List<Video> findByCursoId(UUID id) {
        return videoRepository.findByCursoId(id);
    }

    @Override
    @Transactional
    public void save(Video video) {
        videoRepository.save(video);
    }

    @Transactional(readOnly = true)
    @Override
    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        videoRepository.deleteById(id);

    }

    @Transactional
    @Override
    public Video update(Video video, Long id){
        return videoRepository.save(video);
    }
}
