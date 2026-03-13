package com.vasilevyuv.springcore1.service;

import com.vasilevyuv.springcore1.config.AppProperties;
import com.vasilevyuv.springcore1.exception.NotFoundException;
import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;
import com.vasilevyuv.springcore1.repository.TaskRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ObjectProvider<TaskStatsService> statsServiceProvider;
    private final AppProperties properties;

    public TaskServiceImpl(TaskRepository taskRepository,
                           ObjectProvider<TaskStatsService> statsServiceProvider,
                           AppProperties properties) {
        this.taskRepository = taskRepository;
        this.statsServiceProvider = statsServiceProvider;
        this.properties = properties;
    }

    @Override
    public Task createTask(Task task) {
        if (taskRepository.count() >= properties.getMaxTasks()) {
            throw new IllegalStateException("Достигнут лимит задач: " + properties.getMaxTasks());
        }

        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.valueOf(properties.getDefaultPriority()));
        }
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> getAllTasks(String status, String priority) {
        if (status != null && priority != null) {
            return taskRepository.findByStatusAndPriority(
                    TaskStatus.valueOf(status), TaskPriority.valueOf(priority));
        } else if (status != null) {
            return taskRepository.findByStatus(TaskStatus.valueOf(status));
        } else if (priority != null) {
            return taskRepository.findByPriority(TaskPriority.valueOf(priority));
        } else {
            return taskRepository.findAll();
        }
    }

    @Override
    public Task updateStatus(Long id, TaskStatus newStatus) {
        int updated = taskRepository.updateStatus(id, newStatus);
        if (updated == 0) {
            throw new NotFoundException("Task not found with id: " + id);
        }
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
    }

    @Override
    public Task updateTask(Long id, Task task) {
        int updated = taskRepository.updateTaskById(id, task);
        if (updated == 0) {
            throw new NotFoundException("Task not found with id: " + id);
        }
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found with id: " + id));
    }

    @Override
    public Map<String, Long> getTaskStats() {
        List<Object[]> results = taskRepository.countTasksByStatus();

        Map<String, Long> stats = new HashMap<>();
        for (Object[] result : results) {
            TaskStatus status = (TaskStatus) result[0];
            Long count = (Long) result[1];
            stats.put(status.name(), count);
        }
        for (TaskStatus status : TaskStatus.values()) {
            stats.putIfAbsent(status.name(), 0L);
        }

        return stats;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

}