package com.vasilevyuv.springcore1.service;


import com.vasilevyuv.springcore1.config.AppProperties;
import com.vasilevyuv.springcore1.exception.TaskLimitExceededException;
import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;
import com.vasilevyuv.springcore1.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ObjectProvider<TaskStatsService> statsServiceProvider;
    private final AppProperties appProperties;

    // Constructor injection (R2)
    public TaskServiceImpl(TaskRepository taskRepository,
                           ObjectProvider<TaskStatsService> statsServiceProvider,
                           AppProperties appProperties) {
        this.taskRepository = taskRepository;
        this.statsServiceProvider = statsServiceProvider;
        this.appProperties = appProperties;
    }

    @PostConstruct // R7
    public void init() {
        System.out.println("=== @PostConstruct ===");
        System.out.println("TaskService инициализирован");
    }

    @PreDestroy // R8
    public void destroy() {
        System.out.println("=== @PreDestroy ===");
        System.out.println("Завершение работы. Задач в хранилище: " + taskRepository.findAll().size());
    }

    @Override
    public Task addTask(String title, String description, TaskPriority priority) {
        // Проверка на пустой title (R18)
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title не может быть пустым");
        }

        // Проверка лимита задач (R18)
        int currentTasksCount = taskRepository.findAll().size();
        if (currentTasksCount >= appProperties.getMaxTasks()) {
            throw new TaskLimitExceededException(
                "Достигнут лимит задач (" + appProperties.getMaxTasks() + "). Нельзя добавить больше."
            );
        }

        // Если priority не указан - используем значение по умолчанию (R19)
        if (priority == null) {
            String defaultPriorityStr = appProperties.getDefaultPriority();
            priority = TaskPriority.valueOf(defaultPriorityStr.toUpperCase());
        }

        Task task = new Task(title, description, priority);
        // Статус NEW устанавливается в конструкторе Task

        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public void changeStatus(Long id, TaskStatus status) {
        taskRepository.updateStatus(id, status);
    }

    @Override
    public void printStats() {
        // Получаем новый экземпляр TaskStatsService через ObjectProvider (R14)
        TaskStatsService statsService = statsServiceProvider.getObject();
        statsService.getUuid();
        TaskStatsService statsService2 = statsServiceProvider.getObject();
        statsService2.getUuid();
    }
}