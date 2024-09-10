package com.example.desafio_jr_simplify.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TaskPostRequestDTO(@NotBlank(message = "Task name can't be null/empty") String name, String description,
                                 boolean completed, int priority) {
}
