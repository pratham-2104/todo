package com.dyventech.todo.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class TodoNotFoundExceptionMapper implements ExceptionMapper<TodoNotFoundException> {

    @Override
    public Response toResponse(TodoNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
            .type(MediaType.APPLICATION_JSON)
            .entity(Map.of(
                "error", "Not Found",
                "message", exception.getMessage()
            ))
            .build();
    }
}
