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

    public List<ProductDto> getProductDtoList() {
        List<Product> productList = productRepository.findAll(); // search for all the products in our repository
        List<ProductDto> productDtoList = productMapper.map(productList); // we map our productList to productDtoList
        return productDtoList;
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
