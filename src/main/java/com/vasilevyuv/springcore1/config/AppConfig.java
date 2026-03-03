package com.vasilevyuv.springcore1.config;

import com.vasilevyuv.springcore1.repository.TaskRepository;
import com.vasilevyuv.springcore1.runner.DemoRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DemoRunner demoRunner() {
        return new DemoRunner();
    }

    @Bean
    public TaskRepository taskRepository(@Qualifier("priorityTaskRepository") TaskRepository taskRepository) {
        System.out.println("Получен priorityTaskRepository " + taskRepository.getClass().getSimpleName());
        return taskRepository;
    }
}
