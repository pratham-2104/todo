package com.dyventech.todo.repository;

import com.dyventech.todo.entity.Todo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {

    public List<Todo> findByCompleted(boolean completed) {
        return list("completed", completed);
    }
}
