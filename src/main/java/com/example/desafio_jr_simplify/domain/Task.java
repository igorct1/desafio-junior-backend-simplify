package com.example.desafio_jr_simplify.domain;

import jakarta.persistence.*;

@Entity(name = "tasks")
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String completed;

    private String priority;
}
