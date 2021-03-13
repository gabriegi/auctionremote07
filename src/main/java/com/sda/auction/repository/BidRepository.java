package com.sda.auction.repository;


import com.sda.auction.dto.UserHeaderDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Integer> {

    @Transactional
    @Modifying
    @Query("delete from Bid b where b.user.id IN (SELECT u.id from User u where :userEmail = u.email ) and :productId = b.product.id ")
    void deleteBidByUserAndProduct(@Param("userEmail") String userEmail, @Param("productId") Integer productId);
}
