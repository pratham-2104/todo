package com.dyventech.todo.repository;

import com.dyventech.todo.entity.Todo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TodoRepository implements PanacheRepository<Todo> {

    public List<Todo> findByCompleted(boolean completed) {
        return list("completed = ?1 order by createdAt desc", completed);
    }

    public List<Todo> listAllSorted() {
        return list("order by createdAt desc");
    }

    public long countCompleted() {
        return count("completed", true);
    }
}
