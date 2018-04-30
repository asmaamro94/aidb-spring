package com.example.todos.repository;

import com.example.todos.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>
{
    List<Todo> findAllByOrderByCreatedAtDesc();
}
