package br.com.coupon.domain.exception;

import lombok.Getter;

@Getter
public class InvalidCouponCodeException extends CouponCreationException {

    private final String rejectedCode;

    public InvalidCouponCodeException(String rejectedCode) {
        super("Coupon code must have exactly 6 alphanumeric characters after sanitization, got : '" +  rejectedCode + "'");
        this.rejectedCode = rejectedCode;
    }
}
