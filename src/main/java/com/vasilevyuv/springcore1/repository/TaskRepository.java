package com.vasilevyuv.springcore1.repository;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskStatus;

import java.util.List;

public interface TaskRepository {
    Task save(Task task);
    List<Task> findAll();
    Task delete(Long id);
    void updateStatus(Long id, TaskStatus status);
}
