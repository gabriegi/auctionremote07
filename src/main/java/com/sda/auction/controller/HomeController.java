package com.sda.auction.controller;

import com.sda.auction.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        System.out.println("Se apeleaza get register");
        model.addAttribute("userDto", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(UserDto userDto) {
        System.out.println("Se apeleaza post register cu "+userDto);
        return "register";
    }

}
