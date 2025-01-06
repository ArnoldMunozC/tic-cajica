package com.amunoz.springboot.webflux.ticcajica.controllers;


//import com.amunoz.springboot.webflux.ticcajica.constants.UserConstants;
import com.amunoz.springboot.webflux.ticcajica.constants.UserConstants;
import com.amunoz.springboot.webflux.ticcajica.domain.ResponseDTO;
import com.amunoz.springboot.webflux.ticcajica.exception.CustomerAlreadyExistsException;
import com.amunoz.springboot.webflux.ticcajica.models.User;
import com.amunoz.springboot.webflux.ticcajica.domain.UserDTO;
import com.amunoz.springboot.webflux.ticcajica.services.IEmailService;
import com.amunoz.springboot.webflux.ticcajica.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/users" , produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private IEmailService emailService;

    @PostMapping("/create-user")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody User user){
        Optional<User> optionalUser = Optional.ofNullable(userService.findByEmail(user.getEmail()));
        if (user == null){
           return ResponseEntity.badRequest().build();
        }
        if (optionalUser.isPresent()){
            throw new CustomerAlreadyExistsException("Customer already exists with given email " + user.getEmail());
        }

        user.setCode(String.valueOf(userService.generateUserCode()));
        user.setValidated(0);
        userService.save(user);
        //emailService.sendEmail(user.getEmail(), "Validar cuenta", "Para validar su cuenta, haga click en el siguiente enlace: http://localhost:8080/api/v1/users/validate-user/" + user.getId());
        emailService.sendEmail(user.getEmail(), "Validar cuenta", "Para validar su cuenta, ingrese el siguiente codigo: " + user.getCode());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }


    @PostMapping("/find-user")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        try {
            // Busca el usuario por email y contraseña
            User user1 = userService.findByPasswordAndEmail(userDTO.getPassword(), userDTO.getEmail());

            UserDTO userDTO1 = new UserDTO();
            userDTO1.setEmail(user1.getEmail());
            userDTO1.setPassword(user1.getPassword());

            // Si se encuentra el usuario, se agrega la cookie
            Cookie cookie = new Cookie("userId", String.valueOf(user1.getFirstName().concat(user1.getLastName())));
            response.addCookie(cookie);

            // Retorna el usuario encontrado
            return ResponseEntity.ok(userDTO1);
        } catch (RuntimeException e) {
            // Si no se encuentra el usuario o hay un error, devuelve un error 401 (Unauthorized)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }



    @GetMapping("/find-user/{id}")
    public ResponseEntity<?> findUser(@PathVariable UUID id){
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(optionalUser.get());
    }



    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User user){
        if (id == null || user == null) return ResponseEntity.badRequest().build();

        Optional<User> optionalUser = userService.findById(id);
        if (!optionalUser.isPresent()) return ResponseEntity.notFound().build();

        User user1 = optionalUser.get();
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setEmail(user.getEmail());
        user1.setAge(user.getAge());
        user1.setPassword(user.getPassword());
        user1.setAddress(user.getAddress());
        return ResponseEntity.ok(userService.save(user1));
    }


}
