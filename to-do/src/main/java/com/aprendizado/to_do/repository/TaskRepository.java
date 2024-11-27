package com.aprendizado.to_do.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aprendizado.to_do.model.Task;

public interface TaskRepository extends JpaRepository<Task,Long>{

    List<Task> findByUserId(Long userId);
    
}