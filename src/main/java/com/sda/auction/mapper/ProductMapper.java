package com.sda.auction.mapper;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.model.Product;
import com.sda.auction.model.enums.ProductCategory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<ProductDto> map(List<Product> productList) {

        List<ProductDto> productDtoList = new ArrayList<>(); // we created a new productDtoList

        for (Product product : productList) { // for each product in our productList
            ProductDto productDto = map(product);
            productDtoList.add(productDto); // we add our productDto to the productDtoList
        }

        return productDtoList;
    }

    private ProductDto map(Product product) {
        ProductDto productDto = new ProductDto(); // created a new ProductDto which we populate with product fields

        productDto.setName(product.getName());
        productDto.setCategory(product.getCategory().name());
        productDto.setDescription(product.getDescription());
        productDto.setStartingPrice(product.getStartingPrice().toString());
        productDto.setMinimumBidStep(product.getMinimumBidStep().toString());
        productDto.setStartBiddingTime(product.getStartBiddingTime().toString());
        productDto.setEndBiddingTime(product.getEndBiddingTime().toString());
        String imageAsString = Base64.encodeBase64String(product.getImage());
        productDto.setBase64Image(imageAsString);
        return productDto;
    }
}
