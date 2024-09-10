package com.example.desafio_jr_simplify.integration;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.repository.TaskRepository;
import com.example.desafio_jr_simplify.util.TaskCreator;
import com.example.desafio_jr_simplify.util.TaskPostRequestDTOCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Log4j2
public class TaskControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("listAll list of tasks")
    void list_ReturnsListOfTasks_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        String expectedName = savedTask.getName();

        List<Task> tasks = testRestTemplate.exchange("/tasks", HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>() {
        }).getBody();

        Assertions.assertThat(tasks).isNotNull().isNotEmpty();
        Assertions.assertThat(tasks.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns a task when successful")
    void findById_ReturnsTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        Long expectedId = savedTask.getId();

        Task task = testRestTemplate.getForObject("/tasks/{id}", Task.class, expectedId);
        Assertions.assertThat(task).isNotNull();
        Assertions.assertThat(task.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findById returns a BadRequestException when passed id is invalid")
    void findById_ReturnsBadRequestException_WhenPassedIdIsInvalid() {
        ResponseEntity<Task> taskResponse = testRestTemplate.getForEntity("/tasks/{id}", Task.class, 1);

        Assertions.assertThat(taskResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("save returns task when successful")
    void save_ReturnsTask_WhenSuccessful() {
        TaskPostRequestDTO taskPostRequestDTO = TaskPostRequestDTOCreator.createTaskPostRequestDTO();

        ResponseEntity<Task> taskResponseEntity = testRestTemplate.postForEntity("/tasks", taskPostRequestDTO, Task.class);

        Assertions.assertThat(taskResponseEntity).isNotNull();
        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(taskResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(taskResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("save returns BadRequestException when task name is empty")
    void save_ReturnsBadRequestException_WhenTaskNameIsEmpty() {
        TaskPostRequestDTO taskPostRequestDTO = new TaskPostRequestDTO(null, null, false, 0);
        ResponseEntity<Task> taskResponseEntity = testRestTemplate.postForEntity("/tasks", taskPostRequestDTO, Task.class);

        Assertions.assertThat(taskResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("delete removes task when successful")
    void delete_RemovesTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        ResponseEntity<Void> deletedTask = testRestTemplate.exchange("/tasks/{id}", HttpMethod.DELETE, null, Void.class, savedTask.getId());

        List<Task> tasks = testRestTemplate.exchange("/tasks", HttpMethod.GET, null, new ParameterizedTypeReference<List<Task>>() {
        }).getBody();

        Assertions.assertThat(tasks).isNotNull();
        Assertions.assertThat(tasks).noneMatch(task -> task.getId().equals(savedTask.getId()));
        Assertions.assertThat(deletedTask.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("replace update task when successful")
    void replace_UpdateTask_WhenSuccessful() {
        Task savedTask = taskRepository.save(TaskCreator.createTaskToBeSaved());
        String newName = "modified name";
        savedTask.setName(newName);

        ResponseEntity<Void> taskResponse = testRestTemplate.exchange("/tasks", HttpMethod.PUT,
                new HttpEntity<>(savedTask), Void.class);

        Assertions.assertThat(taskResponse).isNotNull();
        Assertions.assertThat(taskResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Assertions.assertThat(savedTask.getName()).isNotEqualTo(TaskCreator.createTaskToBeSaved().getName());
    }
}
