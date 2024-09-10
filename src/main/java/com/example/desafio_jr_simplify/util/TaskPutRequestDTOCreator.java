package com.example.desafio_jr_simplify.util;

import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;

public class TaskPutRequestDTOCreator {
    public static TaskPutRequestDTO createPutRequestDTO(){
        return TaskPutRequestDTO.builder()
                .id(1L)
                .name("Task put request dto name")
                .description("Task put request dto description")
                .completed(false)
                .priority(1)
                .build();
    }
}
