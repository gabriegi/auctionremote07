package com.sda.auction.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;

    // == relationships ==
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "role")
    private List<User> userList;


}
