package com.aprendizado.to_do.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendizado.to_do.model.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{

    Role findByName(String name);
    
}
