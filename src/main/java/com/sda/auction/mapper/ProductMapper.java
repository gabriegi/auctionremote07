package com.sda.auction.mapper;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.model.enums.ProductCategory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class ProductMapper {

    // mapper -> from productDto to product
    public Product map(ProductDto productDto, MultipartFile multipartFile) {
        Product product = new Product();

        product.setName(productDto.getName());
        product.setCategory(ProductCategory.valueOf(productDto.getCategory()));
        product.setDescription(productDto.getDescription());
        product.setStartingPrice(Integer.valueOf(productDto.getStartingPrice()));
        product.setMinimumBidStep(Integer.valueOf(productDto.getMinimumBidStep()));
        product.setStartBiddingTime(LocalDateTime.parse(productDto.getStartBiddingTime()));
        product.setEndBiddingTime(LocalDateTime.parse(productDto.getEndBiddingTime()));
        try {
            product.setImage(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return product;
    }

    // mapper -> from productList to productDtoList
    public List<ProductDto> map(List<Product> productList, String authenticatedUserEmail) {

        List<ProductDto> productDtoList = new ArrayList<>(); // we created a new productDtoList

        for (Product product : productList) { // for each product in our productList
            ProductDto productDto = map(product, authenticatedUserEmail);
            productDtoList.add(productDto); // we add our productDto to the productDtoList
        }

        return productDtoList;
    }

    public ProductDto map(Product product, String authenticatedUserEmail) {
        ProductDto productDto = new ProductDto(); // created a new ProductDto which we populate with product fields

        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory().name());
        productDto.setDescription(product.getDescription());
        productDto.setStartingPrice(product.getStartingPrice().toString());
        productDto.setMinimumBidStep(product.getMinimumBidStep().toString());
        productDto.setStartBiddingTime(product.getStartBiddingTime().toString());
        productDto.setEndBiddingTime(format(product.getEndBiddingTime()));
        String imageAsString = Base64.encodeBase64String(product.getImage());
        productDto.setBase64Image(imageAsString);
        productDto.setId(product.getId().toString());
        productDto.setCurrentPrice(getCurrentPrice(product));
        productDto.setUserMaximumBid(getMaximumBidOf(product, authenticatedUserEmail));
        productDto.setWinnerAssigned(product.getWinner() != null);
        return productDto;
    }


    // == private methods ==
    private String getMaximumBidOf(Product product, String authenticatedUserEmail) {
        int userMaximumBidValue = 0;
        for (Bid bid : product.getBidList()) { // iterate the bid list
            if (bid.getUser().getEmail().equals(authenticatedUserEmail)) { // if email of user who placed the bid == authenticatedUserEmail
                if (bid.getValue() > userMaximumBidValue) { // compare if bidValue is greater than the maximumBidValue
                    userMaximumBidValue = bid.getValue(); // if yes, we set the new maximumValue
                }
            }
        }
        return String.valueOf(userMaximumBidValue);
    }

    private String format(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTimeFormatter.format(dateTime);
    }

    private String getCurrentPrice(Product product) {
        Optional<Bid> optionalMaxBid = product.getBidList()
                .stream()
                .max(Comparator.comparing(Bid::getValue));
        if (optionalMaxBid.isPresent()) {
            Bid maxBid = optionalMaxBid.get();
            return maxBid.getValue().toString(); // return maxBidValue if present
        }
        return product.getStartingPrice().toString(); // returns starting price if there is no bid present
    }
}
