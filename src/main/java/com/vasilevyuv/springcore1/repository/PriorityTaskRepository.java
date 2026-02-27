package com.vasilevyuv.springcore1.repository;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskStatus;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
@Profile("prod")
public class PriorityTaskRepository implements TaskRepository {
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    public Task save(Task task) {
        task.setId((long) counter.getAndIncrement());
        tasks.add(task);
        return task;
    }
    public List<Task> findAll() {
        return tasks.stream()
                .map(task -> new Task(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getStatus()))
                .sorted(Comparator.comparing(task -> task.getPriority().ordinal()))
                .collect(Collectors.toList());
    }
    public void clear(){
        tasks.clear();
        counter.set(0);
    }

    @Override
    public void updateStatus(Long id, TaskStatus status) {
        Task task = tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Задача с id " + id + " не найдена"));

        task.setStatus(status);
        System.out.println("Статус задачи " + id + " изменен на " + status);
    }

}
