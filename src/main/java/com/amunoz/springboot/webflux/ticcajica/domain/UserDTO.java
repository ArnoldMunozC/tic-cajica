package com.amunoz.springboot.webflux.ticcajica.domain;


import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
}
