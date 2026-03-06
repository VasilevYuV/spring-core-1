package com.vasilevyuv.springcore1.repository;

import com.vasilevyuv.springcore1.exception.NotFoundException;
import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskStatus;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
@Primary
@Profile("dev")
public class InMemoryTaskRepository implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Task save(Task task) {
        task.setId((long) counter.getAndIncrement());
        tasks.add(task);
        return task;
    }

    @Override
    public List<Task> findAll() {
        return tasks.stream()
                .map(task -> new Task(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Задача с id " + id + " не найдена"));
        tasks.remove(task);
    }

    @Override
    public Task updateStatus(Long id, TaskStatus status) {
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Задача с id " + id + " не найдена"));

        task.setStatus(status);
        return task;
    }

    @Override
    public Task findById(Long id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Задача с id " + id + " не найдена"));
    }
}
