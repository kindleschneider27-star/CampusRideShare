package com.example.campusrideshare.api;

import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.security.UserRegistrationForm;
import com.example.campusrideshare.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/users", produces = "application/json")
public class UserRestController {
    private UserService userService;

    public UserRestController(UserService us) {
        this.userService = us;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateRequestDTO request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }
        UserRegistrationForm form = new UserRegistrationForm();
        form.setUsername(request.getUsername());
        form.setPassword(request.getPassword());
        form.setFullName(request.getFullName());
        form.setEmail(request.getEmail());
        form.setDepartment(Department.valueOf(request.getDepartment().toUpperCase()));

        if (!userService.registerNewUser(form)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return userRepo.findByUsername(request.getUsername())
                .map(UserResponseDTO::from)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto))
                .orElse(ResponseEntity.status(HttpStatus.CREATED).build());
    }
}
