package com.amunoz.springboot.webflux.ticcajica.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Table(name = "comments")
@Entity
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private String userId;

    @Column(name = "created_at", updatable = false)
    private String createdAt;

    private String updatedAt;

}
