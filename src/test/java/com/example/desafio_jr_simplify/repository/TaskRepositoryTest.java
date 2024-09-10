package com.example.desafio_jr_simplify.repository;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.util.TaskCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for TaskRepository")
@Log4j2
class TaskRepositoryTest {
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Create should create a new task")
    void save_CreateANewTask() {
        Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
        Task savedTask = taskRepository.save(taskToBeSaved);

        Assertions.assertThat(savedTask).isNotNull();
        Assertions.assertThat(savedTask.getId()).isNotNull();

        Assertions.assertThat(savedTask.getName()).isEqualTo(taskToBeSaved.getName());
    }

    @Test
    @DisplayName("List should return a list of tasks")
    void list_ShouldReturnAListOfTasks() {
        Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
        Task savedTask = taskRepository.save(taskToBeSaved);
        List<Task> tasks = taskRepository.findAll();

        Assertions.assertThat(tasks).isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(tasks.get(0).getId()).isEqualTo(savedTask.getId());
    }

    @Test
    @DisplayName("FindById should return a task when a valid id is passed")
    void findById_ShouldReturnATask_WhenAValidIdIsPassed() {
        Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
        Task savedTask = taskRepository.save(taskToBeSaved);
        Optional<Task> taskById = taskRepository.findById(savedTask.getId());

        Assertions.assertThat(taskById).isNotNull();
        Assertions.assertThat(taskById).contains(savedTask);
    }

    @Test
    @DisplayName("Delete should delete a task")
    void delete_ShouldDeleteATask() {
        Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
        Task savedTask = taskRepository.save(taskToBeSaved);
        taskRepository.deleteById(savedTask.getId());

        List<Task> tasks = taskRepository.findAll();
        Assertions.assertThat(tasks).isEmpty();
        Assertions.assertThat(tasks).doesNotContain(savedTask);
    }

    @Test
    @DisplayName("Replace should update a valid task")
    void replace_ShouldUpdateAValidTask() {
        Task taskToBeSaved = TaskCreator.createTaskToBeSaved();
        Task savedTask = taskRepository.save(taskToBeSaved);
        String newDescription = "new task description";
        savedTask.setDescription(newDescription);

        Task updatedTask = taskRepository.save(savedTask);
        Assertions.assertThat(taskRepository.findById(updatedTask.getId())).isPresent();
        Assertions.assertThat(updatedTask.getId()).isEqualTo(savedTask.getId());
        Assertions.assertThat(updatedTask.getDescription()).isEqualTo(newDescription);
    }
}