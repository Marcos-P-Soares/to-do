package com.aprendizado.to_do.Config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aprendizado.to_do.model.Role;
import com.aprendizado.to_do.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Component
@Order(1)
public class RoleInitializer implements CommandLineRunner{
    
    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        for (Role.Values value : Role.Values.values()) {
            if (roleRepository.findByName(value.name()) == null) {
                var role = new Role();
                role.setName(value.name());
                roleRepository.save(role);
                System.out.println("Role " + value.name() + " criada.");
            } else {
                System.out.println("Role " + value.name() + " já existe.");
            }
        }
    }
}
