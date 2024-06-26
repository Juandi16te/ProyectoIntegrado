package com.example.proyecto_integrado;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SpringsecurityjpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecurityjpaApplication.class, args);
    }

}
