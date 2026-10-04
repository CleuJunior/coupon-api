package br.com.coupon.domain.exception;

public abstract class CouponCreationException extends DomainException {

    public CouponCreationException(String message) {
        super(message);
    }
}
