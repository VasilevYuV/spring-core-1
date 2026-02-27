package com.vasilevyuv.springcore1.runner;


import com.vasilevyuv.springcore1.config.AppProperties;
import com.vasilevyuv.springcore1.exception.TaskLimitExceededException;
import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;
import com.vasilevyuv.springcore1.repository.TaskRepository;
import com.vasilevyuv.springcore1.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AppRunner implements CommandLineRunner {

    private final ApplicationContext context;
    private final TaskService taskService;
    private final AppProperties appProperties;

    // Constructor injection (R2)
    public AppRunner(ApplicationContext context,
                     TaskService taskService,
                     AppProperties appProperties) {
        this.context = context;
        this.taskService = taskService;
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========== ЗАПУСК TASK MANAGER ==========\n");

        // Шаг 1: Приветствие
        step1Greeting();

        // Шаг 2: Добавление задач
        step2AddTasks();

        // Шаг 3: Граничные случаи
        step3BoundaryCases();

        // Шаг 4: Изменение статусов
        step4ChangeStatus();

        // Шаг 5: Prototype и ObjectProvider
        step5PrototypeDemo();

        // Шаг 6: ApplicationContext
        step6ApplicationContextDemo();

        System.out.println("\n========== СЦЕНАРИЙ ЗАВЕРШЕН ==========\n");
    }

    private void step1Greeting() {
        System.out.println("=== Шаг 1: Приветствие ===");
        System.out.println("Добро пожаловать в " + appProperties.getName() +
                "! Лимит: " + appProperties.getMaxTasks() +
                ". Приоритет по умолчанию: " + appProperties.getDefaultPriority());
        System.out.println();
    }

    private void step2AddTasks() {
        System.out.println("=== Шаг 2: Добавление задач ===");
        taskService.addTask("Task LOW", "Description for LOW task", TaskPriority.LOW);
        taskService.addTask("Task MEDIUM", "Description for MEDIUM task", TaskPriority.MEDIUM);
        taskService.addTask("Task HIGH", "Description for HIGH task", null); // null для проверки default priority

        System.out.println("Все задачи после добавления:");
        taskService.getAllTasks().forEach(System.out::println);
        System.out.println();
    }

    private void step3BoundaryCases() {
        System.out.println("=== Шаг 3: Граничные случаи ===");

        // Попытка добавить задачу с пустым title
        try {
            taskService.addTask("", "Empty title", TaskPriority.LOW);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка (пустой title): " + e.getMessage());
        }

        // Попытка превысить лимит
        try {
            System.out.println("Текущее количество задач: " + taskService.getAllTasks().size());
            System.out.println("Пытаемся добавить задачи до превышения лимита...");

            // Добавляем задачи, пока не превысим лимит
            int tasksToAdd = appProperties.getMaxTasks() + 1;
            for (int i = 0; i < tasksToAdd; i++) {
                taskService.addTask("Extra Task " + (i + 1), "Description", TaskPriority.LOW);
                System.out.println("Добавлена задача " + (i + 1) + ". Всего: " + taskService.getAllTasks().size());
            }
        } catch (TaskLimitExceededException e) {
            System.out.println("Ошибка (лимит задач): " + e.getMessage());
        }
        System.out.println();
    }

    private void step4ChangeStatus() {
        System.out.println("=== Шаг 4: Изменение статусов ===");

        List<Task> tasks = taskService.getAllTasks();
        if (!tasks.isEmpty()) {
            // Меняем статус первой задачи на IN_PROGRESS
            taskService.changeStatus(tasks.get(0).getId(), TaskStatus.IN_PROGRESS);
            System.out.println("Задача ID " + tasks.get(0).getId() + " изменена на IN_PROGRESS");

            if (tasks.size() > 1) {
                // Меняем статус второй задачи на DONE
                taskService.changeStatus(tasks.get(1).getId(), TaskStatus.DONE);
                System.out.println("Задача ID " + tasks.get(1).getId() + " изменена на DONE");
            }
        }

        System.out.println("\nDONE задачи:");
        taskService.getAllTasks().stream()
                .filter(t -> t.getStatus() == TaskStatus.DONE)
                .forEach(System.out::println);

        System.out.println("\nIN_PROGRESS задачи:");
        taskService.getAllTasks().stream()
                .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
                .forEach(System.out::println);
        System.out.println();
    }

    private void step5PrototypeDemo() {
        System.out.println("=== Шаг 5: Prototype и ObjectProvider ===");
        System.out.println("Первый вызов printStats():");
        taskService.printStats();

        System.out.println("Второй вызов printStats():");
        taskService.printStats(); // UUID должен быть разный (R15)
        System.out.println();
    }

    private void step6ApplicationContextDemo() {
        System.out.println("=== Шаг 6: ApplicationContext ===");

        // R20: Ручное получение бина через ApplicationContext
        TaskRepository repoFromCtx = context.getBean(TaskRepository.class);
        System.out.println("Бин репозитория из контекста (getBean): " + repoFromCtx.getClass().getSimpleName());

        // R21: Общее количество бинов
        System.out.println("Всего бинов в контексте: " + context.getBeanDefinitionCount());

        // R22: Имена бинов, содержащих "task"
        System.out.println("Бины, содержащие 'task' в имени (без учета регистра):");
        String[] allBeanNames = context.getBeanDefinitionNames();
        Arrays.stream(allBeanNames)
                .filter(name -> name.toLowerCase().contains("task"))
                .forEach(name -> {
                    System.out.println("  - " + name + " (" + context.getBean(name).getClass().getSimpleName() + ")");
                });
        System.out.println();
    }
}