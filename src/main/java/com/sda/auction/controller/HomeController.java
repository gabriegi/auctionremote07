package com.sda.auction.controller;

import com.sda.auction.dto.BidDto;
import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.model.enums.ProductCategory;
import com.sda.auction.service.BidService;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import com.sda.auction.validator.BidValidator;
import com.sda.auction.validator.GenericValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class HomeController {

    // == fields ==
    private final ProductService productService;
    private final GenericValidator genericValidator;
    private final BidValidator bidValidator;
    private final BidService bidService;
    private final UserService userService;

    // == constructor ==
    @Autowired
    public HomeController(ProductService productService, GenericValidator genericValidator,
                          BidValidator bidValidator, BidService bidService, UserService userService) {
        this.productService = productService;
        this.genericValidator = genericValidator;
        this.bidValidator = bidValidator;
        this.bidService = bidService;
        this.userService = userService;
    }

    // == mapping methods ==
    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication, HttpServletRequest request) {
        log.info("getHomePage called");
        boolean categoryExists = true;
        String category = "";

        try {
            category = request.getParameter("category").toUpperCase();
        } catch (NullPointerException e) {
            categoryExists = false;
        }

        ProductCategory categoryId = ProductCategory.valueOf("HOME");

        try {
            categoryId = ProductCategory.valueOf(category);
        } catch(IllegalArgumentException e) {
            categoryExists = false;
        }
        List<ProductDto> productDtoList = productService.getActiveProductDtoList(authentication.getName());

        if(categoryExists) {
           productDtoList = productService.getFilteredProductDtoList(authentication.getName(), categoryId);
        }

        model.addAttribute("productDtoList", productDtoList);
        model.addAttribute("productDto", new ProductDto());
        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);

        return "home";
    }

    @GetMapping("/viewProduct/{productId}")
    public String getViewProduct(Model model, @PathVariable(value = "productId") String productId,
                                 Authentication authentication) {
        if (genericValidator.isNotPositiveInteger(productId)) {
            return "redirect:/home";
        }
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        if (!optionalProductDto.isPresent()) {
            return "redirect:/home";
        }
        ProductDto productDto = optionalProductDto.get();
        model.addAttribute("product", productDto);
        model.addAttribute("bidDto", new BidDto());

        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);

        log.info("Product viewed");
        return "viewProduct";
    }

    @PostMapping("/viewProduct/{productId}")
    public String postBid(Model model, @PathVariable(value = "productId") String productId,
                          BidDto bidDto, BindingResult bindingResult, Authentication authentication) {
        String loggedUserEmail = authentication.getName();
        bidValidator.validate(productId, bidDto, bindingResult);
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId, authentication.getName());
        if (bindingResult.hasErrors()) {

            UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
            model.addAttribute("userHeaderDto", userHeaderDto);

            model.addAttribute("bidDto", bidDto);
            model.addAttribute("product", optionalProductDto.get());
            return "viewProduct";
        }
        log.info("Bid placed");
        bidService.placeBid(bidDto, productId, loggedUserEmail);
        return "redirect:/home";
    }
}
