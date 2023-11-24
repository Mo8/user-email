package com.example.useremail.controllers;

import com.example.useremail.models.Status;
import com.example.useremail.models.User;
import com.example.useremail.repositories.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
public class UserController {

    UserRepository userRepository;
    RabbitTemplate rabbitTemplate;

    public UserController(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("user")
    public User createUser(@RequestBody User user) {
        System.out.println("Create user: " + user);
        user.setStatus(Status.INACTIF);
        user.setValidationCode(String.valueOf(generateCode()));
        User createdUser = userRepository.save(user);
        rabbitTemplate.convertAndSend("user-email", createdUser);
        return createdUser;
    }

    @GetMapping("validate/{code}")
    public void validateUser(@PathVariable String code) {
        User user = userRepository.findUserByValidationCode(code);
        if(user == null) {
            throw new RuntimeException("User not found");
        }
        user.setValidationCode(null);
        user.setStatus(Status.ACTIF);
        userRepository.save(user);
    }


    public static String generateCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int randomDigit = random.nextInt(10);
            codeBuilder.append(randomDigit);
        }
        return codeBuilder.toString();
    }
}
