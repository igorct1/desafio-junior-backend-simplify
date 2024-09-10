package com.example.desafio_jr_simplify.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "tasks")
@Table(name = "tasks")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private boolean completed;

    private int priority;
}
