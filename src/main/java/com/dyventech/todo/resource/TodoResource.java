package com.dyventech.todo.resource;

import com.dyventech.todo.dto.CreateTodoRequest;
import com.dyventech.todo.dto.TodoResponse;
import com.dyventech.todo.dto.UpdateTodoRequest;
import com.dyventech.todo.service.TodoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/api/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {

    @Inject
    TodoService todoService;

    @GET
    public List<TodoResponse> getAll(@QueryParam("completed") Boolean completed) {
        return todoService.getAll(completed);
    }

    @GET
    @Path("/{id}")
    public TodoResponse getById(@PathParam("id") Long id) {
        return todoService.getById(id);
    }

    @POST
    public Response create(@Valid CreateTodoRequest request) {
        TodoResponse created = todoService.create(request);
        return Response.created(URI.create("/api/todos/" + created.id()))
            .entity(created)
            .build();
    }

    @PUT
    @Path("/{id}")
    public TodoResponse update(@PathParam("id") Long id, @Valid UpdateTodoRequest request) {
        return todoService.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        todoService.delete(id);
        return Response.noContent().build();
    }
}
