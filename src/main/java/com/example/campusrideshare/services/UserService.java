package com.example.campusrideshare.services;

import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.domain.User;
import com.example.campusrideshare.security.UserRegistrationForm;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository ur,PasswordEncoder pe) {
        userRepo = ur;
        passwordEncoder = pe;
    }


    // add in the users if missing for development
    public void createUserIfMissing(User user) {
        if (!userRepo.existsByUsername(user.getUsername())) {
            logger.info("User created: {}", user.getUsername());
            userRepo.save(user);
        }
        else {
            logger.info("User already exists: {}", user.getUsername());
        }
    }

   @Transactional
    public boolean registerNewUser(UserRegistrationForm form) {
        // check unique username
        String username = form.getUsername().trim();
        if (userRepo.existsByUsername(username)) {
            return false;
        }

        // encode password
        String encodedPassword = passwordEncoder.encode(form.getPassword());

        // create user from form
        User newUser = new User(username, encodedPassword, form.getFullName(), form.getEmail(),
                form.getDepartment(), "ROLE_STUDENT"); // set the role to user always

        userRepo.save(newUser);

        return true; // success!!
    }

    @Transactional
    public Optional<User> updateUser(User user) {
        Optional<User> newUser = userRepo.findByUsername(user.getUsername());
        if (newUser.isPresent()) {
            newUser.get().setFullName(user.getFullName());
            newUser.get().setEmail(user.getEmail());
            newUser.get().setDepartment(user.getDepartment());
            newUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(newUser.get());

        }
        else {}
        return newUser;
    }
}
