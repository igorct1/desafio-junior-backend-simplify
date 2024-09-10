package com.example.desafio_jr_simplify.service;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.exception.BadRequestExceptionTask;
import com.example.desafio_jr_simplify.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    public Task findByIdOrThrowBadRequest(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new BadRequestExceptionTask("Task not found, please enter a valid id"));
    }

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public void replace(Task task) {
        taskRepository.save(task);
    }

    public void delete(long id) {
        taskRepository.deleteById(id);
    }
}
