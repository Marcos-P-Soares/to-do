package com.aprendizado.to_do.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;

import com.aprendizado.to_do.DTO.CreateUserDTO;
import com.aprendizado.to_do.DTO.TaskOutputDTO;
import com.aprendizado.to_do.DTO.UserOutputDTO;
import com.aprendizado.to_do.model.Role;
import com.aprendizado.to_do.model.User;
import com.aprendizado.to_do.repository.RoleRepository;
import com.aprendizado.to_do.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<User> createUser (CreateUserDTO dto){

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDB = userRepository.findByEmail(dto.Email());
        if ((userFromDB.isPresent())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Email já está em uso.");
        }

        var user = new User();
        user.setUserName(dto.userName());
        user.setEmail(dto.Email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(Set.of(basicRole));
        
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    public List<UserOutputDTO> getUserList(){
        var users = userRepository.findAll();

        return users.stream().map(user -> new UserOutputDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getTasks().stream().map(task -> new TaskOutputDTO(
                        task.getTitle(),
                        task.getDescription(),
                        task.isCompleted(),
                        task.getPriority()
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
    
    

}
