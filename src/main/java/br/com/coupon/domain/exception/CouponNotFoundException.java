package br.com.coupon.domain.exception;

public class CouponNotFoundException extends DomainException {

    public CouponNotFoundException(Long id) {
        super("Coupon not found with id " + id);
    }
}
