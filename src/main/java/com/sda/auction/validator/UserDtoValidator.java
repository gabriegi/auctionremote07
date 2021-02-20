package com.sda.validator;

import com.sda.repository.UserRepository;
import com.sda.dto.UserDto;
import com.sda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Optional;

@Service
public class UserDtoValidator {

    private UserRepository userRepository;

    @Autowired
    public UserDtoValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validate(UserDto userDto, BindingResult bindingResult) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()){
            bindingResult.addError(new FieldError("userDto","email","This email is already used!"));
        }
    }
}
