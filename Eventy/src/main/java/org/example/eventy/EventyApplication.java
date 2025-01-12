package org.example.eventy;

import jakarta.annotation.PostConstruct;
import org.example.eventy.users.models.Role;
import org.example.eventy.users.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EventyApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventyApplication.class, args);
    }
}
