package com.sda.auction.service;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.mapper.ProductMapper;
import com.sda.auction.model.Product;
import com.sda.auction.model.User;
import com.sda.auction.repository.ProductRepository;
import com.sda.auction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // == fields ==
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;

    // == constructor ==
    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userRepository = userRepository;
    }

    // == public methods ==
    public void addProduct(ProductDto productDto, String loggedUserEmail, MultipartFile multipartFile) {
        Product product = productMapper.map(productDto, multipartFile);
        assignSeller(loggedUserEmail, product);
        productRepository.save(product);
    }

    public List<ProductDto> getProductDtoList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAll(); // search for all the products in our repository
        return productMapper.map(productList, authenticatedUserEmail);
    }

    public List<ProductDto> getActiveProductDtoList(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllActive(LocalDateTime.now());
        return productMapper.map(productList, authenticatedUserEmail);
    }

    public List<ProductDto> getProductDtoListByBidder(String authenticatedUserEmail) {
        List<Product> productList = productRepository.findAllByBidder(authenticatedUserEmail);
        return productMapper.map(productList, authenticatedUserEmail);
    }

    public Optional<ProductDto> getProductDtoBy(String productId, String authenticatedUserEmail) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.parseInt(productId)); // search product by id
        // if optionalProduct is not present we return an empty container
        if (!optionalProduct.isPresent()) {
            return Optional.empty();
        }
        ProductDto productDto = productMapper.map(optionalProduct.get(), authenticatedUserEmail);
        return Optional.of(productDto); // return an optional of productDto
    }



    // == private methods ==
    private void assignSeller(String loggedUserEmail, Product product) {
        Optional<User> optionalUser = userRepository.findByEmail(loggedUserEmail); // we return a container with User

        if (optionalUser.isPresent()) {
            User user = optionalUser.get(); // we return a user from the container
            product.setSeller(user); // we set the user to the seller
        }
    }



}
