package com.sda.model;

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

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "role")
    private List<User> users;

}
