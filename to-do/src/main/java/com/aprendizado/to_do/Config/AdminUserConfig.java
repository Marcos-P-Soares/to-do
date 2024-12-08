package com.aprendizado.to_do.Config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.aprendizado.to_do.model.Role;
import com.aprendizado.to_do.model.User;
import com.aprendizado.to_do.repository.RoleRepository;
import com.aprendizado.to_do.repository.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class AdminUserConfig implements CommandLineRunner{
    
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    public BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        var roleAdmin = roleRepository.findByName(Role.Values.ADMIN.name());
        if (roleAdmin == null) {
            System.err.println("Role ADMIN não encontrada no banco de dados.");
        }

        var userAdmin = userRepository.findByEmail("admin_mahattan@email.com");

        userAdmin.ifPresentOrElse(
            _ -> System.out.println("ADMIN já existe")
            ,
            () -> {
                var user = new User();
                user.setUserName("admin");
                user.setEmail("admin_mahattan@email.com");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(Set.of(roleAdmin));
                userRepository.save(user);
            }
            );

    }
}
