package com.example.desafio_jr_simplify.service;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;
import com.example.desafio_jr_simplify.exception.BadRequestExceptionTask;
import com.example.desafio_jr_simplify.mapper.TaskMapper;
import com.example.desafio_jr_simplify.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    public Task findByIdOrThrowBadRequest(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new BadRequestExceptionTask("Task not found, please enter a valid id"));
    }

    @Transactional
    public Task create(TaskPostRequestDTO taskPostRequestDTO) {
        Task task = TaskMapper.INSTANCE.toTask(taskPostRequestDTO);
        return taskRepository.save(task);
    }

    public void replace(TaskPutRequestDTO taskPutRequestDTO) {
        Task task = TaskMapper.INSTANCE.toTask(taskPutRequestDTO);
        taskRepository.save(task);
    }

    public void delete(long id) {
        taskRepository.deleteById(id);
    }
}
