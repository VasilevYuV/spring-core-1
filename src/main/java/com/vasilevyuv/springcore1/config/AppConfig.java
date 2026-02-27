package com.vasilevyuv.springcore1.config;

import com.vasilevyuv.springcore1.runner.DemoRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DemoRunner demoRunner() {
        return new DemoRunner();
    }
}
