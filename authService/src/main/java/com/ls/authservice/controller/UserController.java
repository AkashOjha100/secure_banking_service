package com.ls.authservice.controller;

import com.ls.authservice.domain.User;
import com.ls.authservice.dto.RegisterRequest;
import com.ls.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User addUser(@RequestBody RegisterRequest request){
        return userService.register(request);
    }
}
