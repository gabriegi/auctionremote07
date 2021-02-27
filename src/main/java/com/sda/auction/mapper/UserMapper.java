package com.sda.auction.mapper;

import com.sda.auction.dto.UserDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {

    // will map the user to the userDto
    public User map(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setDateOfBirth(LocalDate.parse(userDto.getDateOfBirth()));

        // returns the mapped user
        return user;
    }

    public UserHeaderDto map(User user) {
        UserHeaderDto userHeaderDto = new UserHeaderDto();
        userHeaderDto.setFirstName(user.getFirstName());

        return userHeaderDto;
    }
}
