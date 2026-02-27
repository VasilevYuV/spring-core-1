package com.vasilevyuv.springcore1.service;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;

import java.util.List;

public interface TaskService {
    Task addTask(String title, String description, TaskPriority priority);
    List<Task> getAllTasks();
    void changeStatus(Long id, TaskStatus status);
    void printStats();
}