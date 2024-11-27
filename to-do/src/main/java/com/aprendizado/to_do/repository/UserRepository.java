package com.aprendizado.to_do.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendizado.to_do.model.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByName(String name);
}
