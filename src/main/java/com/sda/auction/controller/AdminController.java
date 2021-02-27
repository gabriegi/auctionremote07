package com.sda.auction.controller;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import com.sda.auction.validator.ProductDtoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
public class AdminController {

    // == fields ==
    private final ProductDtoValidator productDtoValidator;
    private final ProductService productService;
    private final UserService userService;

    // == constructor ==
    @Autowired
    public AdminController(ProductDtoValidator productDtoValidator, ProductService productService,
                           UserService userService) {
        this.productDtoValidator = productDtoValidator;
        this.productService = productService;
        this.userService = userService;
    }

    // == mapping methods ==
    @GetMapping("/addProduct")
    public String getAddProduct(Model model, Authentication authentication) {
        log.info("getAddProduct called");
        model.addAttribute("productDto", new ProductDto());

        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);

        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String postAddProduct(Model model, ProductDto productDto, BindingResult bindingResult,
                                 Authentication authentication, @RequestParam("image") MultipartFile multipartFile) {
        System.out.println(multipartFile);
        String loggedUserEmail = authentication.getName();
        productDtoValidator.validate(productDto, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("productDto", productDto);
            return "addProduct";
        }
        log.info("Product added");
        productService.addProduct(productDto, loggedUserEmail, multipartFile);
        return "redirect:/addProduct";
    }

}
