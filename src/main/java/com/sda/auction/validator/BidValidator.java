package com.sda.auction.validator;

import com.sda.auction.dto.BidDto;
import com.sda.auction.model.Bid;
import com.sda.auction.model.Product;
import com.sda.auction.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Comparator;
import java.util.Optional;

@Service
public class BidValidator {

    // == fields ==
    private final ProductRepository productRepository;

    // == constructor ==
    @Autowired
    public BidValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // == public methods ==
    public void validate(String productId, BidDto bidDto, BindingResult bindingResult) {
        Optional<Product> optionalProduct = productRepository.findById(Integer.valueOf(productId));
        if (!optionalProduct.isPresent()) {
            bindingResult.addError(new FieldError("bidDto", "value", "Invalid product Id"));
            return;
        }

        validateBidValue(bidDto, bindingResult, optionalProduct.get());

    }

    // == private methods ==
    private void validateBidValue(BidDto bidDto, BindingResult bindingResult, Product product) {

        if (isBidValueNotNumber(bidDto)) {
            bindingResult.addError(new FieldError("bidDto", "value", "This field should be a number."));
            return;
        }

        Optional<Bid> optionalMaxBid = getMaxBid(product);
        int bidDtoValue = Integer.parseInt(bidDto.getValue());
        int productCurrentPrice = product.getStartingPrice(); // variable to store the currentPrice
        boolean isError = false;
        String errorMessage = null;

        if (optionalMaxBid.isPresent()) {
            productCurrentPrice = optionalMaxBid.get().getValue();
            if (bidDtoValue <= productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the last bid!";
            }
        } else {
            if (bidDtoValue < productCurrentPrice) {
                isError = true;
                errorMessage = "Value is smaller than the starting price!";
            }
        }
        if (isError) {
            bindingResult.addError(new FieldError("bidDto", "value", errorMessage));
        }

    }

    private Optional<Bid> getMaxBid(Product product) {
        return product.getBidList() // gets the BidList
                .stream() // created stream with max method
                .max(Comparator.comparing(Bid::getValue));
    }

    private boolean isBidValueNotNumber(BidDto value) {
        try {
            Integer.parseInt(value.getValue());
        } catch (NumberFormatException numberFormatException) {
            return true;
        }
        return false;
    }
}
