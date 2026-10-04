package br.com.coupon.domain;

import br.com.coupon.domain.exception.CouponAlreadyDeletedException;
import br.com.coupon.domain.exception.InvalidDiscountValueException;
import br.com.coupon.domain.exception.PastExpirationDateException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Coupon {

    private Long id;
    private final CouponCode code;
    private final String description;
    private final BigDecimal discountValue;
    private final LocalDate expirationDate;
    private final boolean published;
    private LocalDateTime deletedAt;

    public static Coupon of(String code, String description, BigDecimal discountValue, LocalDate expirationDate, boolean published) {
        CouponCode couponCode = CouponCode.of(code);

        if (discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidDiscountValueException(discountValue);
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new PastExpirationDateException(expirationDate);
        }

        return new Coupon(couponCode, description, discountValue, expirationDate, published);
    }

    public void delete() {
    if (Objects.nonNull(deletedAt)) {
            throw  new CouponAlreadyDeletedException();
        }

        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
