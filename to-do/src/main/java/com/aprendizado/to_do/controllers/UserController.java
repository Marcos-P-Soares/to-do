package com.aprendizado.to_do.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.to_do.DTO.CreateUserDTO;
import com.aprendizado.to_do.DTO.UserOutputDTO;
import com.aprendizado.to_do.service.UserService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDTO) {
        try {
            var user = userService.createUser(createUserDTO);
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            return ResponseEntity.status(422).body(ex.getMessage());
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<UserOutputDTO>> ListUsers(){
        var users = userService.getUserList();
        return ResponseEntity.ok(users);
    }
}
