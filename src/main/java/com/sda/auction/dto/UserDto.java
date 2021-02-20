package com.sda.dto;

import lombok.Data;

@Data
public class UserDto {

    private String firstname;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String password;
    private Boolean isAdmin;


}
