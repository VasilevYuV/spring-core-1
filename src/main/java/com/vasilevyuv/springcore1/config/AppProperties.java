package com.vasilevyuv.springcore1.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties {
    @Value("${app.name:Unknown}")
    private String name;
    @Value("${app.max-tasks:5}")
    private int maxTasks;
    @Value("${app.default-priority:LOW}")
    private String defaultPriority;

    @PostConstruct
    private void settings() {
        System.out.println("Загруженные настройки: Имя: " + name +
                ", Лимит: " + maxTasks +
                ". Приоритет по умолчанию: " + defaultPriority);
        System.out.println();
    }
}
