package com.example.springbrokerapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.springbrokerapp")
public class AppConfig {

    @Bean
    public Broker getBroker() {
        return new Broker();
    }
}
