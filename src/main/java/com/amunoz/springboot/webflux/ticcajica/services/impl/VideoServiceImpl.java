package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.Video;
import com.amunoz.springboot.webflux.ticcajica.repositories.VideoRepository;
import com.amunoz.springboot.webflux.ticcajica.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public void save(Video video) {
        videoRepository.save(video);
    }

    @Override
    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        videoRepository.deleteById(id);

    }

    @Override
    public Video update(Video video, Long id){
        return videoRepository.save(video);
    }
}
