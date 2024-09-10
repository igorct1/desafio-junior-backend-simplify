package com.example.desafio_jr_simplify.controller;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;
import com.example.desafio_jr_simplify.service.TaskService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Log4j2
class TaskControllerTest {
    @InjectMocks
    private TaskController taskController;
    @Mock
    private TaskService taskServiceMock;

    @BeforeEach
    void setUp() {
        BDDMockito.given(taskServiceMock.listAll())
                .willReturn(List.of(TaskCreator.createValidTask()));

        BDDMockito.given(taskServiceMock.create(ArgumentMatchers.any(TaskPostRequestDTO.class)))
                .willReturn(TaskCreator.createValidTask());

        BDDMockito.given(taskServiceMock.findByIdOrThrowBadRequest(ArgumentMatchers.anyLong()))
                .willReturn(TaskCreator.createValidTask());

        BDDMockito.doNothing().when(taskServiceMock).delete(ArgumentMatchers.anyLong());

        BDDMockito.doNothing().when(taskServiceMock).replace(ArgumentMatchers.any(TaskPutRequestDTO.class));
    }

    @Test
    @DisplayName("List returns a list of Tasks")
    void list_ReturnsAListOfTasks() {
        Task task = TaskCreator.createValidTask();
        ResponseEntity<List<Task>> list = taskController.list();
        Assertions.assertThat(list.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Task> tasks = list.getBody();

        Assertions.assertThat(tasks).isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(task.getName());
    }

    @Test
    @DisplayName("Save returns task when successful")
    void save_ReturnTaskWhenSuccessful() {
        ResponseEntity<Task> response = taskController.save(TaskPostRequestDTOCreator.createTaskPostRequestDTO());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Task createdTask = response.getBody();
        Assertions.assertThat(createdTask).isNotNull();
        Assertions.assertThat(createdTask).isEqualTo(TaskCreator.createValidTask());
    }

    @Test
    @DisplayName("Find by id return a task")
    void findById_ReturnTaskWhenSuccessful() {
        ResponseEntity<Task> response = taskController.findById(TaskCreator.createValidTask().getId());
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Task task = response.getBody();
        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isEqualTo(TaskCreator.createValidTask().getId());
    }

    @Test
    @DisplayName("Delete removes a task")
    void delete_RemovesTaskWhenSuccessful() {
        ResponseEntity<Void> response = taskController.delete(1L);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        BDDMockito.verify(taskServiceMock).delete(1L);
    }

    @Test
    @DisplayName("Replace should update a task")
    void replace_ShouldUpdateATask() {
        TaskPutRequestDTO putRequestDTO = TaskPutRequestDTOCreator.createPutRequestDTO();
        ResponseEntity<Void> response = taskController.replace(putRequestDTO);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        BDDMockito.verify(taskServiceMock).replace(putRequestDTO);
    }
}