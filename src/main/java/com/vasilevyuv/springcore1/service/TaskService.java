package com.vasilevyuv.springcore1.service;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Task addTask(String title, String description, TaskPriority priority);
    List<Task> getAllTasks(String status, String priority);
    Task getTaskById(Long id);
    Task changeStatus(Long id, TaskStatus status);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    Map<String, Long> getTaskStats();
}