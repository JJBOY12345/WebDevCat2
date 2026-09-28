package com.shopwebsite.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(UserRepository repository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (repository.findByEmailIgnoreCase("admin@shop.local").isEmpty()) {
                repository.save(new User("Store Admin", "admin@shop.local", encoder.encode("admin123"), "ADMIN"));
            }
            if (repository.findByEmailIgnoreCase("demo@shop.local").isEmpty()) {
                repository.save(new User("Demo Customer", "demo@shop.local", encoder.encode("demo123"), "CUSTOMER"));
            }
            if (repository.findByEmailIgnoreCase("student@shop.local").isEmpty()) {
                repository.save(new User("Student Customer", "student@shop.local", encoder.encode("student123"), "CUSTOMER"));
            }
        };
    }
}
