package com.amunoz.springboot.webflux.ticcajica.services.impl;

import com.amunoz.springboot.webflux.ticcajica.models.User;
import com.amunoz.springboot.webflux.ticcajica.repositories.UserRepository;
import com.amunoz.springboot.webflux.ticcajica.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }


    @Override
    public Optional<User> findById(UUID id) {
        return Optional.of((User) userRepository.findById(id).orElseThrow());
    }


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(User user, UUID id) {
        userRepository.save(user);
    }


    @Override
    public User findByPasswordAndEmail(String password, String email) {
        return userRepository.findByPasswordAndEmail(password, email);
    }

    @Override
    public String generateUserCode() {
        Random random = new Random();
        int number = random.nextInt(899999) + 100000;
        return String.valueOf(number);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
