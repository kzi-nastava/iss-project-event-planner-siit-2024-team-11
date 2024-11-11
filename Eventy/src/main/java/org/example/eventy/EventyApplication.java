package org.example.eventy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EventyApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventyApplication.class, args);
    }

}
