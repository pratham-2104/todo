package com.dyventech.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateTodoRequest(
    @NotBlank(message = "Title is required and cannot be blank")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    String title,

    String description,

    @NotNull(message = "Completed status is required")
    Boolean completed
) {
}
