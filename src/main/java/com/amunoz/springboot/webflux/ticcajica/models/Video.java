package com.amunoz.springboot.webflux.ticcajica.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "videos")
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    @Column(name = "video_path")
    private String videoPath;


}
