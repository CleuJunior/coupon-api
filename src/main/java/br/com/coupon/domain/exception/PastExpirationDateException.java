package br.com.coupon.domain.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PastExpirationDateException extends CouponCreationException {

    private final LocalDate rejectedDate;

    public PastExpirationDateException(LocalDate rejectedDate) {
        super("Expiration date cannot be in the past, got '" + rejectedDate + "'");
        this.rejectedDate = rejectedDate;
    }
}
