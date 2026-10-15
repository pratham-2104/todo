package com.dyventech.todo.dto;

import com.dyventech.todo.entity.Todo;

import java.time.Instant;

public record TodoResponse(
    Long id,
    String title,
    String description,
    boolean completed,
    Instant createdAt,
    Instant updatedAt
) {
    public static TodoResponse fromEntity(Todo todo) {
        return new TodoResponse(
            todo.getId(),
            todo.getTitle(),
            todo.getDescription(),
            todo.isCompleted(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}
