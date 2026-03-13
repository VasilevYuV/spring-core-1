package com.vasilevyuv.springcore1.service;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task);
    List<Task> getAllTasks(String status, String priority);
    Optional<Task> getTaskById(Long id);
    Task updateStatus(Long id, TaskStatus status);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    Map<String, Long> getTaskStats();
}