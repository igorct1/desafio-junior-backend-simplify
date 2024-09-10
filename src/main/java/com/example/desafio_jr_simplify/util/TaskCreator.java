package com.example.desafio_jr_simplify.util;

import com.example.desafio_jr_simplify.domain.Task;

public  class TaskCreator {
    public static Task createTaskToBeSaved(){
        return Task.builder()
                .name("Task")
                .description("Task description")
                .completed(false)
                .priority(1)
                .build();
    }
}
