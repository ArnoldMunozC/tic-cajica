package com.amunoz.springboot.webflux.ticcajica.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;


    private String firstName;
    private String lastName;
    private String username;

    @Column(unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_at")
    private Date createAt;
    private int age;

    private int validated;

    private String code;

    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Address address;


    @PrePersist
    public void prePersist() {
        createAt = new Date();
    }

}
