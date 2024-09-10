package com.example.desafio_jr_simplify.dto;

import lombok.Builder;

@Builder
public record TaskPostRequestDTO(String name, String description, boolean completed, int priority) {
}
