package com.dyventech.todo.service;

import com.dyventech.todo.dto.CreateTodoRequest;
import com.dyventech.todo.dto.TodoResponse;
import com.dyventech.todo.dto.UpdateTodoRequest;
import com.dyventech.todo.entity.Todo;
import com.dyventech.todo.exception.TodoNotFoundException;
import com.dyventech.todo.repository.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class TodoService {

    @Inject
    TodoRepository todoRepository;

    public List<TodoResponse> getAll(Boolean completed) {
        List<Todo> todos;
        if (completed != null) {
            todos = todoRepository.findByCompleted(completed);
        } else {
            todos = todoRepository.listAll();
        }
        return todos.stream()
            .map(TodoResponse::fromEntity)
            .toList();
    }

    public TodoResponse getById(Long id) {
        Todo todo = todoRepository.findByIdOptional(id)
            .orElseThrow(() -> new TodoNotFoundException(id));
        return TodoResponse.fromEntity(todo);
    }

    @Transactional
    public TodoResponse create(CreateTodoRequest request) {
        Todo todo = new Todo();
        todo.setTitle(request.title().trim());
        todo.setDescription(request.description() != null ? request.description().trim() : null);
        todo.setCompleted(false);

        todoRepository.persist(todo);
        return TodoResponse.fromEntity(todo);
    }

    @Transactional
    public TodoResponse update(Long id, UpdateTodoRequest request) {
        Todo todo = todoRepository.findByIdOptional(id)
            .orElseThrow(() -> new TodoNotFoundException(id));

        todo.setTitle(request.title().trim());
        todo.setDescription(request.description() != null ? request.description().trim() : null);
        todo.setCompleted(request.completed());

        return TodoResponse.fromEntity(todo);
    }

    @Transactional
    public void delete(Long id) {
        boolean deleted = todoRepository.deleteById(id);
        if (!deleted) {
            throw new TodoNotFoundException(id);
        }
    }
}
