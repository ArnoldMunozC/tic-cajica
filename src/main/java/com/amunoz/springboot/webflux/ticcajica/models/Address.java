package com.amunoz.springboot.webflux.ticcajica.models;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "address")
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String primerParametro;
    private String segundoParametro;
    private String tercerParametro;
    private String cuartoParametro;

    @OneToOne(mappedBy = "address")
    private User user;
}
