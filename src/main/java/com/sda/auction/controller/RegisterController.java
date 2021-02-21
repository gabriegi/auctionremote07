package com.sda.auction.controller;

import com.sda.auction.dto.UserDto;
import com.sda.auction.service.UserService;
import com.sda.auction.validator.UserDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller // controls the flow of data
@Slf4j
public class RegisterController {

    // == fields ==
    private final UserService userService;
    private final UserDtoValidator userDtoValidator;

    // == constructor ==
    @Autowired
    public RegisterController(UserService userService, UserDtoValidator userDtoValidator) {
        this.userService = userService;
        this.userDtoValidator = userDtoValidator;
    }

    // == mapping methods ==
    @GetMapping("/register")
    // Model - we keep data inside ( Model View Controller contains Model)
    public String getRegisterPage(Model model) {
        log.info("getRegisterPage called");
        model.addAttribute("userDto", new UserDto());

        return "register";
    }

    @PostMapping("/register")                 // BindingResult - will collect our errors
    public String postRegisterPage(Model model, UserDto userDto, BindingResult bindingResult) {
        log.info("postRegisterPage called");
        userDtoValidator.validate(userDto, bindingResult); // will validate the userDto
        if (bindingResult.hasErrors()) {
            model.addAttribute("userDto", userDto); // the old data will remain
            return "register"; // if it has error we return to register page
        }
        log.info("User registered successfully");
        userService.register(userDto);
        return "redirect:/home"; // if registered redirect to home
    }
}
