package com.sda.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    private com.sda.model.Role role;



}
