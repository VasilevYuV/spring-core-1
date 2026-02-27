package com.vasilevyuv.springcore1.runner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

public class DemoRunner implements CommandLineRunner {
    @Value("${app.name:Unknown}")
    private String name;
    @Value("${app.max-tasks:5}")
    private int maxTasks;
    @Value("${app.default-priority:LOW}")
    private String defaultPriority;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Добро пожаловать в " + name + "! Лимит: " + maxTasks + ". Приоритет по умолчанию: " + defaultPriority);

    }
}
