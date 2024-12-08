package com.aprendizado.to_do.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.to_do.DTO.LoginRequest;
import com.aprendizado.to_do.DTO.LoginResponse;
import com.aprendizado.to_do.model.Role;
import com.aprendizado.to_do.repository.UserRepository;

@RestController
public class TokenController {
    
    private final JwtEncoder jwtEncoder;

    private final UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    TokenController(JwtEncoder jwtEncoder, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        
        var user = userRepository.findByEmail(loginRequest.email());
       
        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("Email or password is invalid!");
        }
        
        var scopes = user.get().getRoles()
                            .stream()
                            .map(Role::getName)
                            .collect(Collectors.joining(" "));


        var now = Instant.now();
        var expiresIn = 1800L;

        var claims = JwtClaimsSet.builder()
        .issuer("mybackend")
        .subject(user.get().getUserId().toString())
        .issuedAt(now)
        .expiresAt(now.plusSeconds(expiresIn))
        .claim("scope", scopes)
        .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        
        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }

    
}
