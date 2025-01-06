package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.Comments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentsRepository extends CrudRepository<Comments, Long> {

}
