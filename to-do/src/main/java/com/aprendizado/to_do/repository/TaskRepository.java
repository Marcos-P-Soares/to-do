package com.aprendizado.to_do.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aprendizado.to_do.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
    List<Task> findByTitle(String title);
    
}
