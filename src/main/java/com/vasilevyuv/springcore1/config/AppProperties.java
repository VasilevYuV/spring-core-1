package com.vasilevyuv.springcore1.config;

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
}
