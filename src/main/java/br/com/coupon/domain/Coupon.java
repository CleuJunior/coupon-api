package br.com.coupon.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon {

    private final CouponCode code;
    private final String description;
    private final BigDecimal discountValue;
    private final LocalDate expirationDate;
    private final boolean published;
    private LocalDateTime deletedAt;

    public static Coupon of(String code, String description, BigDecimal discountValue, LocalDate expirationDate, boolean published) {
        CouponCode couponCode = CouponCode.of(code);
        return new Coupon(couponCode, description, discountValue, expirationDate, published);
    }

    public void delete() {
        //TODO implementar regra para deletar
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
