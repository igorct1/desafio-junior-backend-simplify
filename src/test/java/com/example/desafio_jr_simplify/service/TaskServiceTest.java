package com.example.desafio_jr_simplify.service;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;
import com.example.desafio_jr_simplify.exception.BadRequestExceptionTask;
import com.example.desafio_jr_simplify.repository.TaskRepository;
import com.example.desafio_jr_simplify.util.TaskCreator;
import com.example.desafio_jr_simplify.util.TaskPostRequestDTOCreator;
import com.example.desafio_jr_simplify.util.TaskPutRequestDTOCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@Log4j2
class TaskServiceTest {
    @InjectMocks
    private TaskService taskService;
    @Mock
    private TaskRepository taskRepositoryMock;

    @BeforeEach
    void setUp() {
        BDDMockito.given(taskRepositoryMock.findAll(BDDMockito.any(Sort.class)))
                .willReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.given(taskRepositoryMock.save(ArgumentMatchers.any(Task.class)))
                .willReturn(TaskCreator.createValidTask());

        BDDMockito.given(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.of(TaskCreator.createValidTask()));

        BDDMockito.doNothing().when(taskRepositoryMock).deleteById(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List should returns a list of valid tasks")
    void list_ShouldReturnAListOfValidTasks() {
        String expectedName = TaskCreator.createValidTask().getName();
        List<Task> tasks = taskService.listAll();

        Assertions.assertThat(tasks).isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Create should persist a new task")
    void create_ShouldPersistANewTask() {
        Task createdTask = taskService.create(TaskPostRequestDTOCreator.createTaskPostRequestDTO());

        Assertions.assertThat(createdTask).isNotNull();
        Assertions.assertThat(createdTask).isEqualTo(TaskCreator.createValidTask());
    }

    @Test
    @DisplayName("Replace update task when successful")
    void replace_UpdateTask_WhenSuccessful() {
        Assertions.assertThatCode(() -> taskService.replace(TaskPutRequestDTOCreator.createPutRequestDTO()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Find by id should return a task")
    void findById_ShouldReturnATask() {
        Long expectedId = TaskCreator.createValidTask().getId();
        Task taskById = taskService.findByIdOrThrowBadRequest(expectedId);

        Assertions.assertThat(taskById).isNotNull();
        Assertions.assertThat(taskById.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Find by id throws a BadRequestExceptionTask when task is not found")
    void findById_ThrowsBadRequestExceptionTask_WhenTaskIsNotFound() {
        BDDMockito.given(taskRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestExceptionTask.class)
                .isThrownBy(() -> taskService.findByIdOrThrowBadRequest(1));
    }

    @Test
    @DisplayName("Delete should delete a task")
    void delete_ShouldDeleteATask() {
        Assertions.assertThatCode(() -> taskService.delete(1))
                .doesNotThrowAnyException();
    }
}