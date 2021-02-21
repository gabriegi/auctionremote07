package com.sda.auction.validator;

import org.springframework.stereotype.Service;

@Service
public class GenericValidator {

    public boolean isNotPositiveInteger(String someString) {

        try {
            int number = Integer.parseInt(someString);
            return number <= 0;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
