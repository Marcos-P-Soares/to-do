package com.aprendizado.to_do.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aprendizado.to_do.DTO.CreateTaskDTO;
import com.aprendizado.to_do.DTO.TaskOutputDTO;
import com.aprendizado.to_do.model.Role;
import com.aprendizado.to_do.model.Task;
import com.aprendizado.to_do.model.User;
import com.aprendizado.to_do.repository.TaskRepository;
import com.aprendizado.to_do.repository.UserRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public void createTask(CreateTaskDTO taskDTO, JwtAuthenticationToken token) {
        var user = validateUserExists(UUID.fromString(token.getName()));
        var task = buildTask(taskDTO, user);
        taskRepository.save(task);
    }

    public void deleteTask(Long taskID, JwtAuthenticationToken token) {
        var user = validateUserExists(UUID.fromString(token.getName()));
        var task = validateTaskExists(taskID);

        if (!isUserAuthorizedToDeleteTask(user, task)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não autorizado a deletar esta tarefa");
        }

        taskRepository.deleteById(taskID);
    }

    public List<TaskOutputDTO> getTasksByUserId(UUID userId) {
        var user = validateUserExists(userId);
        return buildTaskOutputList(user.getTasks());
    }

    private boolean isUserAuthorizedToDeleteTask(User user, Task task) {
        var isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals(Role.Values.ADMIN.name()));
        return task.getUser().getUserId().equals(user.getUserId()) || isAdmin;
    }

    private User validateUserExists(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    private Task validateTaskExists(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task não encontrada"));
    }

    private Task buildTask(CreateTaskDTO taskDTO, User user) {
        var task = new Task();
        task.setUser(user);
        task.setTitle(taskDTO.title());
        task.setDescription(taskDTO.description());
        task.setPriority(taskDTO.priority());
        return task;
    }

    private List<TaskOutputDTO> buildTaskOutputList(List<Task> tasks) {
        List<TaskOutputDTO> taskOutputList = new ArrayList<>();
        for (Task task : tasks) {
            taskOutputList.add(new TaskOutputDTO(
                    task.getTitle(),
                    task.getDescription(),
                    task.isCompleted(),
                    task.getPriority()
            ));
        }
        return taskOutputList;
    }
}
