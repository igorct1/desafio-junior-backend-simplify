package com.example.desafio_jr_simplify.mapper;

import com.example.desafio_jr_simplify.domain.Task;
import com.example.desafio_jr_simplify.dto.TaskPostRequestDTO;
import com.example.desafio_jr_simplify.dto.TaskPutRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {
    public static final TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
    public abstract Task toTask(TaskPostRequestDTO task);
    public abstract Task toTask(TaskPutRequestDTO task);
}
