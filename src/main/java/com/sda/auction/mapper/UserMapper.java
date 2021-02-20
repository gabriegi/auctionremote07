package com.sda.mapper;

import com.sda.dto.UserDto;
import com.sda.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {

    public User map(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstname());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setDateOfBirth(LocalDate.parse(userDto.getDateOfBirth()));
        user.setPassword(userDto.getPassword());
        return user;
    }
}
