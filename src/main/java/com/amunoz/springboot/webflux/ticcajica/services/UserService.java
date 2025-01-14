package com.amunoz.springboot.webflux.ticcajica.services;

import com.amunoz.springboot.webflux.ticcajica.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(UUID id);

    User save(User user);

    void deleteById(UUID id);

    void update(User user, UUID id);

    User findByPasswordAndEmail(String password, String email);

    String generateUserCode();

    User findByEmail(String email);


}
