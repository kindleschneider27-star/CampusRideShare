package com.example.campusrideshare;

import com.example.campusrideshare.domain.User;
import com.example.campusrideshare.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CampusRideShareApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusRideShareApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner dataLoader(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            userService.createUserIfMissing(new User("student", passwordEncoder.encode("student123"),
                    "Lilly Lane", "llane@mccneb.edu","IT","ROLE_STUDENT"));
            userService.createUserIfMissing((new User("admin", passwordEncoder.encode("admin123"),
                    "Kindle Schneider", "kschneider@mccneb.edu", "ENG", "ROLE_ADMIN")));
        };
    }
}
