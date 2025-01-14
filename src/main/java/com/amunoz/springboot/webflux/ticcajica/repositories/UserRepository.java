package com.amunoz.springboot.webflux.ticcajica.repositories;

import com.amunoz.springboot.webflux.ticcajica.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.password = ?1 AND u.email = ?2")
    User findByPasswordAndEmail(String password, String email);

    User findByEmail(String email);


    void deleteById(UUID id);

    Optional<User> findById(UUID id);

    Optional<User> findByUsername(String username);

}
