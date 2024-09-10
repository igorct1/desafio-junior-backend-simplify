package com.example.desafio_jr_simplify.controller;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;
import com.example.desafio_jr_simplify.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
@Log4j2
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> list() {
        return ResponseEntity.ok(taskService.listAll());
    }

    @PostMapping
    public ResponseEntity<Task> save(@RequestBody TaskPostRequestDTO taskPostRequestDTO) {
        return new ResponseEntity<>(taskService.create(taskPostRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> findById(@PathVariable long id) {
        return ResponseEntity.ok(taskService.findByIdOrThrowBadRequest(id));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody TaskPutRequestDTO taskPutRequestDTO){
        taskService.replace(taskPutRequestDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
