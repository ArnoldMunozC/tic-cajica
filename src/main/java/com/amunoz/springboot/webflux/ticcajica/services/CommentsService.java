package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.Comments;

import java.util.List;

public interface CommentsService {

    List<Comments> findAll();
    Comments findById(Long id);
    void save(Comments comment);
    Void delete(Comments comment);
    Comments update(Comments comment);
}
