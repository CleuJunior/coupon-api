package br.com.coupon.domain.exception;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InvalidDiscountValueException extends CouponCreationException {

    private final BigDecimal discountValue;

    public InvalidDiscountValueException(BigDecimal discountValue) {
        super("Discount value must be at least 0.5, got: '" + discountValue + "'");
        this.discountValue = discountValue;
    }
}
