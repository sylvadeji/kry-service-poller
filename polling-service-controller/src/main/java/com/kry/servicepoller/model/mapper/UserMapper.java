package com.kry.servicepoller.model.mapper;

import com.kry.servicepoller.model.UserModel;
import com.kry.servicepoller.model.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;

@Service
public class UserMapper {

    @Autowired
    ModelMapper mapper;

    public UserModel mapToModel(UserDto userDto) {
        UserModel userModel = mapper.map(userDto, UserModel.class);
        userModel.setDateCreated(Objects.isNull(userModel.getDateCreated()) ?
                ZonedDateTime.now() : userModel.getDateCreated());
        return userModel;
    }

    public UserDto mapFromModel(UserModel userModel) {
        UserDto userDto = mapper.map(userModel, UserDto.class);
        return userDto;
    }
}
