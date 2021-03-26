package com.kry.servicepoller.service;

import com.kry.servicepoller.model.dto.UserDto;

import java.util.List;

public interface UserService {
    public List<UserDto> getAllUsers();
    public UserDto createUser(UserDto user);
}
