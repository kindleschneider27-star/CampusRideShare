package com.example.campusrideshare.api;

import com.example.campusrideshare.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/api/users", produces = "application/json")
public class UserRestController {
    private UserService userService;

    public UserRestController(UserService us) {
        this.userService = us;
    }


}
