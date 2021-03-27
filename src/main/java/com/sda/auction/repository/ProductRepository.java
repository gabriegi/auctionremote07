package com.sda.auction.repository;

import com.sda.auction.model.Product;
import com.sda.auction.model.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("select p from Product p where :now between p.startBiddingTime and p.endBiddingTime")
        // gives all the products which are in between start and end bidding time
    List<Product> findAllActive(@Param("now") LocalDateTime now);

    @Query("select p from Product p where :category = p.category and :now between p.startBiddingTime and p.endBiddingTime ")
        // this method filters products by category id
    List<Product> filterAllActive(@Param("now") LocalDateTime now, @Param("category") ProductCategory category);

    @Query("select distinct b.product from Bid b where :authenticatedUserEmail = b.user.email")
        // will show us only the products the logged in user bidded
    List<Product> findAllByBidder(@Param("authenticatedUserEmail") String authenticatedUserEmail);

    @Query("select p from Product p where :now > p.endBiddingTime and p.winner is null")
    List<Product> findAllExpiredAndUnassigned(@Param("now") LocalDateTime now);
}
