package com.amunoz.springboot.webflux.ticcajica.controllers;

import com.amunoz.springboot.webflux.ticcajica.models.Comments;
import com.amunoz.springboot.webflux.ticcajica.services.CommentsService;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/comments")
public class CommentsControler {

    private final CommentsService commentsService;

    public CommentsControler(CommentsService commentsService) {
        this.commentsService = commentsService;
    }


    @GetMapping("/all-comments")
    public List<Comments> getAllComments() {
        return commentsService.findAll();
    }


    @PostMapping("/create-comment")
    public void createComment(@RequestBody Comments comment) {

        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        comment.setCreatedAt(formattedDate);
        commentsService.save(comment);
    }

    @PostMapping("/delete-comment")
    public void deleteComment(@RequestBody Comments comment) {

        if (comment == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        commentsService.delete(comment);
    }

    @PutMapping("/update-comment")
    public void updateComment(@RequestBody Comments comment) {
        if (comment.getId() == null || comment.getComment() == null) {
            throw new IllegalArgumentException("Comment cannot be null");
        }
        Comments comm = commentsService.findById(comment.getId());
        if (comm != null) {
            comm.setComment(comment.getComment());
            // Convertir Date a String con formato "dd-MM-yyyy"
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            comm.setUpdatedAt(formattedDate);
            commentsService.update(comm);
        } else {
            throw new IllegalArgumentException("Comment not found");
        }
    }




}
