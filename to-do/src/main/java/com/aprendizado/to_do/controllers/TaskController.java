package com.aprendizado.to_do.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.to_do.model.Task;
import com.aprendizado.to_do.repository.TaskRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getTasks(@RequestParam (required = false) String title) {
        if (title!= null){
            return taskRepository.findByTitle(title);
        }
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById (@PathVariable Long id){
        Optional<Task> optional = taskRepository.findById(id);
        if(!optional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optional.get());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody @Valid Task task){
        taskRepository.save(task);
        return ResponseEntity.status(201).body(task);

    }
    
}
