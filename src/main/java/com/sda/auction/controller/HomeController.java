package com.sda.auction.controller;

import com.sda.auction.dto.BidDto;
import com.sda.auction.dto.ProductDto;
import com.sda.auction.service.BidService;
import com.sda.auction.service.ProductService;
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

    // == constructor ==
    @Autowired
    public HomeController(ProductService productService, GenericValidator genericValidator,
                          BidValidator bidValidator, BidService bidService) {
        this.productService = productService;
        this.genericValidator = genericValidator;
        this.bidValidator = bidValidator;
        this.bidService = bidService;
    }

    // == mapping methods ==
    @GetMapping("/home")
    public String getHomePage(Model model) {
        log.info("getHomePage called");
        List<ProductDto> productDtoList = productService.getProductDtoList(); // we call a service to receive a productDtoList
        model.addAttribute("productDtoList", productDtoList);
        return "home";
    }

    @GetMapping("/viewProduct/{productId}")
    public String getViewProduct(Model model, @PathVariable(value = "productId") String productId) {
        if (genericValidator.isNotPositiveInteger(productId)) {
            return "redirect:/home";
        }
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId);
        if (!optionalProductDto.isPresent()) {
            return "redirect:/home";
        }
        ProductDto productDto = optionalProductDto.get();
        model.addAttribute("product", productDto);
        model.addAttribute("bidDto", new BidDto());
        log.info("Product viewed");
        return "viewProduct";
    }

    @PostMapping("/viewProduct/{productId}")
    public String postBid(Model model, @PathVariable(value = "productId") String productId,
                          BidDto bidDto, BindingResult bindingResult, Authentication authentication) {
        String loggedUserEmail = authentication.getName();
        bidValidator.validate(productId, bidDto, bindingResult);
        Optional<ProductDto> optionalProductDto = productService.getProductDtoBy(productId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("bidDto", bidDto);
            model.addAttribute("product", optionalProductDto.get());
            return "viewProduct";
        }
        log.info("Bid placed");
        bidService.placeBid(bidDto, productId, loggedUserEmail);
        return "redirect:/home";
    }
}
