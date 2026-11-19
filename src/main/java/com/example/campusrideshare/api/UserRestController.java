package com.example.campusrideshare.api;

import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.domain.Department;
import com.example.campusrideshare.security.UserRegistrationForm;
import com.example.campusrideshare.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/users", produces = "application/json")
public class UserRestController {
    private UserService userService;
    private UserRepository userRepo;

    public UserRestController(UserService us, UserRepository ur) {
        this.userService = us;
        this.userRepo = ur;
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

    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id,
                                                  @Valid @RequestBody UserUpdateRequestDTO request) {
        Department department;
        try {
            department = Department.valueOf(request.getDepartment().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return userService.updateUser(id, request.getFullName(), request.getEmail(), department, null)
                .map(UserResponseDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
