package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.Comments;
import com.amunoz.springboot.webflux.ticcajica.repositories.CommentsRepository;
import com.amunoz.springboot.webflux.ticcajica.services.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;


    @Override
    public List<Comments> findAll() {
        return (List<Comments>) commentsRepository.findAll();
    }

    @Override
    public Comments findById(Long id) {
        return commentsRepository.findById(id).orElse(null);
    }

    public void save(Comments comment) {
        commentsRepository.save(comment);
    }

    @Override
    public Void delete(Comments comment) {
        commentsRepository.delete(comment);
        return null;
    }

    @Override
    public Comments update(Comments comment) {
        return commentsRepository.save(comment);
    }
}
