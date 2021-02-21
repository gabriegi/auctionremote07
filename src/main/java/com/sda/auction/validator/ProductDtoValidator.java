package com.sda.auction.validator;

import com.sda.auction.dto.ProductDto;
import com.sda.auction.model.enums.ProductCategory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Service
public class ProductDtoValidator {

    // == public methods ==
    public void validate(ProductDto productDto, BindingResult bindingResult) {

        validateName(productDto.getName(), bindingResult);
        validateDescription(productDto.getDescription(), bindingResult);
        validateCategory(productDto.getCategory(), bindingResult);
        validateStartingBid(productDto.getStartingPrice(), bindingResult);
        validateMinimumBidStep(productDto.getMinimumBidStep(), bindingResult);

        boolean isStartBiddingTimeValid = validateStartBidingTime(productDto.getStartBiddingTime(), bindingResult);
        boolean isEndBiddingTimeValid = validateEndBidingTime(productDto.getEndBiddingTime(), bindingResult);
        if (isStartBiddingTimeValid && isEndBiddingTimeValid) {
            validateBothBiddingTime(productDto.getStartBiddingTime(), productDto.getEndBiddingTime(), bindingResult);
        }
    }

    // == private methods ==
    private void validateCategory(String category, BindingResult bindingResult) {
        try {
            ProductCategory.valueOf(category);
        } catch (IllegalArgumentException exception) {
            bindingResult.addError(new FieldError("productDto", "category", "Wrong category."));
        }
    }

    private void validateDescription(String description, BindingResult bindingResult) {
        if (description.isEmpty() || description.length() < 10) {
            bindingResult.addError(
                    new FieldError("productDto", "description", "Description is too short. Should contain at least 10 characters!"));
        }
    }

    private void validateName(String name, BindingResult bindingResult) {
        if (name.isEmpty()) {
            bindingResult.addError(
                    new FieldError("productDto", "name", "Name cannot be empty."));
        }
    }

    private void validateStartingBid(String startingBid, BindingResult bindingResult) {
        validateFieldAsPositiveInteger("startingPrice", startingBid, bindingResult);
    }

    private void validateMinimumBidStep(String minimumBidStep, BindingResult bindingResult) {
        validateFieldAsPositiveInteger("minimumBidStep", minimumBidStep, bindingResult);
    }

    // generic method to validate number
    private void validateFieldAsPositiveInteger(String fieldName, String fieldValue, BindingResult bindingResult) {
        try {
            Integer fieldValueAsInteger = Integer.parseInt(fieldValue);
            if (fieldValueAsInteger <= 0) {
                bindingResult.addError(new FieldError("productDto", fieldName, "This field should be positive."));
            }
        } catch (NumberFormatException numberFormatException) {
            bindingResult.addError(new FieldError("productDto", fieldName, "This field should be a number."));
        }
    }

    private boolean validateStartBidingTime(String startBiddingTime, BindingResult bindingResult) {
        return isDateTimeFormatValid("startBiddingTime", startBiddingTime, bindingResult);
    }

    private boolean validateEndBidingTime(String endBiddingTime, BindingResult bindingResult) {
        return isDateTimeFormatValid("endBiddingTime", endBiddingTime, bindingResult);
    }

    private boolean isDateTimeFormatValid(String fieldName, String fieldValue, BindingResult bindingResult) {
        try {
            LocalDateTime.parse(fieldValue);
        } catch (DateTimeParseException e) {
            bindingResult.addError(new FieldError("productDto", fieldName, "Invalid Format"));
            return false;
        }
        return true;
    }

    private void validateBothBiddingTime(String startBiddingTime, String endBiddingTime, BindingResult bindingResult) {
        LocalDateTime start = LocalDateTime.parse(startBiddingTime);
        LocalDateTime end = LocalDateTime.parse(endBiddingTime);
        if (end.isBefore(start) || end.isBefore(LocalDateTime.now())) {
            bindingResult.addError(new FieldError("productDto", "endBiddingTime", "Should be in the future and after bidding time."));
        }
    }
}
