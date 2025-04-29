package com.amunoz.springboot.webflux.ticcajica.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
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
