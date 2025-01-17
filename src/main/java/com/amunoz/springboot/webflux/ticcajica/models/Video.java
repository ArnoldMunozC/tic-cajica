package com.amunoz.springboot.webflux.ticcajica.models;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
@Table(name = "videos")
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    @Column(name = "video_path", length = 500)
    private String videoPath;

    @Column(name = "curso_id")
    private UUID cursoId;


}
