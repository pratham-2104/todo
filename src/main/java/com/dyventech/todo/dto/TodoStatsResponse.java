package com.dyventech.todo.dto;

public record TodoStatsResponse(
    long total,
    long completed,
    long pending
) {
}
