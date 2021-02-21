package com.sda.auction.mapper;

import com.sda.auction.dto.BidDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.model.User;
import org.springframework.stereotype.Component;

@Component
public class BidMapper {


    public Bid map(BidDto bidDto, Product product, User user) {

        Bid bid = new Bid();
        bid.setValue(Integer.valueOf(bidDto.getValue()));
        bid.setProduct(product);
        bid.setUser(user);
        return bid;
    }
}
