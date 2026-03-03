package com.vasilevyuv.springcore1.service;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Getter
@Component
@Scope("prototype") // R13
public class TaskStatsService {

    private String uuid;

    public void getUuid() {
        // Генерируем UUID при создании каждого экземпляра (R13)
        this.uuid = UUID.randomUUID().toString();
        System.out.println("UUID: " + uuid);
    }

}