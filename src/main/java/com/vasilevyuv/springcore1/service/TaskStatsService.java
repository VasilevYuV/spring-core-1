package com.vasilevyuv.springcore1.service;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.UUID;

@Getter
@Component
@Scope("prototype") // R13
public class TaskStatsService {

    private String uuid;

    @PostConstruct
    public void init() {
        // Генерируем UUID при создании каждого экземпляра (R13)
        this.uuid = UUID.randomUUID().toString();
        System.out.println("=== TaskStatsService @PostConstruct ===");
        System.out.println("Сгенерирован новый UUID: " + uuid);
    }

}