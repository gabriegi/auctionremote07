package com.sda.dto;

import lombok.Data;

@Data
public class ProductDto {

    private String name;
    private String description;
    private String category;
    private String startingPrice;
    private String minimumBidStep;
    private String startBiddingTime;
    private String endBiddingTime;
}
