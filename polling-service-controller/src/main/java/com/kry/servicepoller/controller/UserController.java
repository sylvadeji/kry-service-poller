package com.kry.servicepoller.controller;

import com.kry.servicepoller.model.dto.UserDto;
import com.kry.servicepoller.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUser() {

        return userService.getAllUsers();
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createService(@RequestBody UserDto user) {
        return userService.createUser(user);
    }
}
