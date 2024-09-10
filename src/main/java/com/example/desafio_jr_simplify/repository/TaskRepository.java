package com.example.desafio_jr_simplify.repository;

import com.example.desafio_jr_simplify.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
