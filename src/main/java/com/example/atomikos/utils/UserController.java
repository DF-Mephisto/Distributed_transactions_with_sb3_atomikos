package com.example.atomikos.utils;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    public UserController(final UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping
    void createUser(final @RequestBody UserInput input)
    {
        userService.createUser(input);
    }
}
