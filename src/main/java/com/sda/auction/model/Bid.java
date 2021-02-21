package com.sda.auction.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Bid {

    // == fields ==
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer value;

    // == relationships ==
    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;
}
