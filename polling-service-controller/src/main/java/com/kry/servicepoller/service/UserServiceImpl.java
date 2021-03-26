package com.kry.servicepoller.service;

import com.kry.servicepoller.model.UserModel;
import com.kry.servicepoller.model.dto.UserDto;
import com.kry.servicepoller.model.mapper.UserMapper;
import com.kry.servicepoller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/***
 * User Management Service
 * To manage service users
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper mapper;

    @Override
    public List<UserDto> getAllUsers() {

        List<UserDto> users;

        users = userRepository.findAll().stream()
                .map(user -> mapper.mapFromModel(user))
                .collect(Collectors.toList());

        return users;
    }

    @Override
    public UserDto createUser(UserDto user) {

        UserModel userModel = mapper.mapToModel(user);
        if (Objects.nonNull(userModel)) {
            userRepository.save(userModel);
        }
        return mapper.mapFromModel(userModel);

    }
}
