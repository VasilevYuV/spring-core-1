package com.vasilevyuv.springcore1.service;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;
import com.vasilevyuv.springcore1.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    // Constructor injection (R2)
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task addTask(String title, String description, TaskPriority priority) {
        Task task = new Task(title, description, priority);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks(String status, String priority) {
        List<Task> allTasks = taskRepository.findAll();
        return allTasks.stream()
                .filter(task -> filterByStatus(task, status))
                .filter(task -> filterByPriority(task, priority))
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task changeStatus(Long id, TaskStatus status) {
        return taskRepository.updateStatus(id, status);
    }

    @Override
    public Task updateTask(Long id, Task newTask) {
        Task task = getTaskById(id);
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setPriority(newTask.getPriority());
        return task;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.delete(id);
    }

    @Override
    public Map<String, Long> getTaskStats() {
        List<Task> allTasks = taskRepository.findAll();

        return allTasks.stream()
                .collect(Collectors.groupingBy(
                        task -> task.getStatus().name(),
                        Collectors.counting()
                ));
    }

    private boolean filterByStatus(Task task, String status) {
        if (status == null || status.trim().isEmpty()) {
            return true;
        }
        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            return task.getStatus() == taskStatus;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status +
                    ". Allowed values: NEW, IN_PROGRESS, DONE");
        }
    }

    private boolean filterByPriority(Task task, String priority) {
        if (priority == null || priority.trim().isEmpty()) {
            return true;
        }
        try {
            TaskPriority taskPriority = TaskPriority.valueOf(priority.toUpperCase());
            return task.getPriority() == taskPriority;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority: " + priority +
                    ". Allowed values: LOW, MEDIUM, HIGH");
        }
    }

    private long countByStatus(List<Task> tasks, TaskStatus status) {
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .count();
    }

    private long countByPriority(List<Task> tasks, TaskPriority priority) {
        return tasks.stream()
                .filter(t -> t.getPriority() == priority)
                .count();
    }
}