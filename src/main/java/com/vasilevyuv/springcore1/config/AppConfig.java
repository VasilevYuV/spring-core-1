package com.vasilevyuv.springcore1.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    /*@Bean
    @Profile("prod")
    public TaskRepository taskRepository(@Qualifier("priorityTaskRepository") TaskRepository taskRepo) {
        System.out.println("Получен priorityTaskRepository " + taskRepo.getClass().getSimpleName());
        return taskRepo;
    }*/
}
