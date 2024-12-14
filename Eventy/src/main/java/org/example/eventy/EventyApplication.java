package org.example.eventy;

import jakarta.annotation.PostConstruct;
import org.example.eventy.users.models.Role;
import org.example.eventy.users.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EventyApplication {
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(EventyApplication.class, args);
    }

    @PostConstruct
    public void seedRoles() {
        roleRepository.save(new Role("Admin"));
        roleRepository.save(new Role("AuthenticatedUser"));
        roleRepository.save(new Role("Organizer"));
        roleRepository.save(new Role("Provider"));
    }
}
