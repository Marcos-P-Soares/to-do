package com.aprendizado.to_do.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.aprendizado.to_do.DTO.CreateTaskDTO;
import com.aprendizado.to_do.DTO.TaskOutputDTO;

import com.aprendizado.to_do.service.TaskService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController( TaskService taskService) {

        this.taskService = taskService;
    }

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@RequestBody CreateTaskDTO taskDTO, JwtAuthenticationToken token) {
        taskService.createTask(taskDTO, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long taskID, JwtAuthenticationToken token) {
        taskService.deleteTask(taskID, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tasks/{userId}")
    public ResponseEntity<List<TaskOutputDTO>> tasks (@PathVariable("userID") UUID userId) {
        var tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
    
}
