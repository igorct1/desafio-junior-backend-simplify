package com.example.desafio_jr_simplify.util;

import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;

public class TaskPostRequestDTOCreator {
    public static TaskPostRequestDTO createTaskPostRequestDTO(){
        return TaskPostRequestDTO.builder()
                .name("Task post request dto name")
                .description("Task post request dto description")
                .completed(false)
                .priority(1)
                .build();
    }
}
