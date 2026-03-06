package com.vasilevyuv.springcore1.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "priority", "status"})
public class Task {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;

    public Task(String title, String description, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.priority = priority;
    }

    public Task(Long id, String title, String description, TaskPriority priority) {
        this(title, description, priority);
        this.id = id;
    }

    public Task(Long id, String title, String description, TaskPriority priority, TaskStatus status) {
        this(id, title, description, priority);
        this.status = status;
    }
}