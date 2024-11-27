package com.aprendizado.to_do.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.to_do.model.User;
import com.aprendizado.to_do.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user){
        userRepository.save(user);
        return ResponseEntity.status(201).body(user);
    }
    
    @GetMapping
    public List<User> get(@RequestParam (required = false) String name){
        if(name == null){
            return userRepository.findAll();
        }
        return userRepository.findByName(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById (@PathVariable Long id){
        Optional<User> optional = userRepository.findById(id);
        if(!optional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }
}
