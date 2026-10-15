package com.dyventech.todo.service;

import com.dyventech.todo.dto.CreateTodoRequest;
import com.dyventech.todo.dto.TodoResponse;
import com.dyventech.todo.dto.TodoStatsResponse;
import com.dyventech.todo.dto.UpdateTodoRequest;
import com.dyventech.todo.entity.Todo;
import com.dyventech.todo.exception.TodoNotFoundException;
import com.dyventech.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoService todoService;

    private Todo sampleTodo;

    @BeforeEach
    void setUp() {
        sampleTodo = new Todo();
        sampleTodo.setId(1L);
        sampleTodo.setTitle("Buy milk");
        sampleTodo.setDescription("2 gallons of whole milk");
        sampleTodo.setCompleted(false);
        sampleTodo.setCreatedAt(Instant.now());
        sampleTodo.setUpdatedAt(Instant.now());
    }

    @Test
    void testGetAllWithoutFilter() {
        when(todoRepository.listAllSorted()).thenReturn(List.of(sampleTodo));

        List<TodoResponse> result = todoService.getAll(null);

        assertEquals(1, result.size());
        assertEquals("Buy milk", result.get(0).title());
        verify(todoRepository).listAllSorted();
        verify(todoRepository, never()).findByCompleted(anyBoolean());
    }

    @Test
    void testGetStats() {
        when(todoRepository.count()).thenReturn(10L);
        when(todoRepository.countCompleted()).thenReturn(4L);

        TodoStatsResponse stats = todoService.getStats();

        assertEquals(10L, stats.total());
        assertEquals(4L, stats.completed());
        assertEquals(6L, stats.pending());
    }

    @Test
    void testGetAllWithCompletedFilter() {
        when(todoRepository.findByCompleted(true)).thenReturn(List.of());

        List<TodoResponse> result = todoService.getAll(true);

        assertTrue(result.isEmpty());
        verify(todoRepository).findByCompleted(true);
        verify(todoRepository, never()).listAll();
    }

    @Test
    void testGetByIdFound() {
        when(todoRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleTodo));

        TodoResponse result = todoService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Buy milk", result.title());
    }

    @Test
    void testGetByIdNotFound() {
        when(todoRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        assertThrows(TodoNotFoundException.class, () -> todoService.getById(99L));
    }

    @Test
    void testCreate() {
        doAnswer(invocation -> {
            Todo todo = invocation.getArgument(0);
            todo.setId(10L);
            todo.setCreatedAt(Instant.now());
            todo.setUpdatedAt(Instant.now());
            return null;
        }).when(todoRepository).persist(any(Todo.class));

        CreateTodoRequest request = new CreateTodoRequest("Clean room", "Vacuum the floor");
        TodoResponse result = todoService.create(request);

        assertNotNull(result);
        assertEquals("Clean room", result.title());
        assertEquals("Vacuum the floor", result.description());
        assertFalse(result.completed());
        verify(todoRepository).persist(any(Todo.class));
    }

    @Test
    void testUpdateSuccess() {
        when(todoRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleTodo));

        UpdateTodoRequest request = new UpdateTodoRequest("Buy oat milk", "Organic oat milk", true);
        TodoResponse result = todoService.update(1L, request);

        assertEquals("Buy oat milk", result.title());
        assertEquals("Organic oat milk", result.description());
        assertTrue(result.completed());
    }

    @Test
    void testUpdateNotFound() {
        when(todoRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        UpdateTodoRequest request = new UpdateTodoRequest("Title", "Desc", true);
        assertThrows(TodoNotFoundException.class, () -> todoService.update(99L, request));
    }

    @Test
    void testDeleteSuccess() {
        when(todoRepository.deleteById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> todoService.delete(1L));
        verify(todoRepository).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(todoRepository.deleteById(99L)).thenReturn(false);

        assertThrows(TodoNotFoundException.class, () -> todoService.delete(99L));
    }
}
