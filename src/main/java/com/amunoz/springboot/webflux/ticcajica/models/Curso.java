package com.amunoz.springboot.webflux.ticcajica.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "cursos")
@Entity
public class Curso {

    @Id
    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String nombre;

    private String descripcion;

    @Lob
    @JsonIgnore
    private byte[] imagen;


    public Integer getImagenHashCode() {
        return (this.imagen != null) ? this.imagen.hashCode(): null;
    }

}
