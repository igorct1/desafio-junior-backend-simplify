package com.example.desafio_jr_simplify.dto;

import lombok.Builder;

@Builder
public record TaskPutRequestDTO(Long id, String name, String description, boolean completed, int priority) {
}
