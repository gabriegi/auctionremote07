package com.sda.auction.controller;
import com.sda.auction.dto.ProductDto;
import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.service.ProductService;
import com.sda.auction.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.sda.auction.repository.BidRepository;

import java.util.List;

@Slf4j
@Controller
public class MyBidsController {

    // == fields ==
    private final ProductService productService;
    private final UserService userService;
    private final BidRepository bidRepository;

    // == constructor ==
    @Autowired
    public MyBidsController(ProductService productService, UserService userService, BidRepository bidRepository) {
        this.productService = productService;
        this.userService = userService;
        this.bidRepository = bidRepository;
    }

    @GetMapping("/myBids")
    public String getMyBids(Model model, Authentication authentication) {
        log.info("getMyBids called");
        List<ProductDto> productDtoList = productService.getProductDtoListByBidder(authentication.getName());
        model.addAttribute("productDtoList", productDtoList);

        UserHeaderDto userHeaderDto = userService.getUserHeaderDto(authentication.getName());
        model.addAttribute("userHeaderDto", userHeaderDto);

        return "myBids";
    }

    @PostMapping("/removeBid/{productId}")
    public String postRemoveBid(Model model, @PathVariable(value = "productId") Integer productId,
                                Authentication authentication) {
        String userEmail = authentication.getName();
        bidRepository.deleteBidByUserAndProduct(userEmail, productId);

        return "redirect:/home";
    }
}
