package br.com.coupon.fixture;

import br.com.coupon.infra.web.dto.CreateCouponRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CouponRequestFixture {

    private static final String DEFAULT_CODE = "ABC123";
    private static final String DEFAULT_DESCRIPTION = "10% off";
    private static final BigDecimal DEFAULT_DISCOUNT_VALUE = new BigDecimal("10.00");
    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.now().plusDays(1);

    private CouponRequestFixture() {
    }

    public static CreateCouponRequest valid() {
        return build(DEFAULT_CODE, DEFAULT_DESCRIPTION, DEFAULT_DISCOUNT_VALUE, DEFAULT_EXPIRATION_DATE, false);
    }

    public static CreateCouponRequest alreadyPublished() {
        return build(DEFAULT_CODE, DEFAULT_DESCRIPTION, DEFAULT_DISCOUNT_VALUE, DEFAULT_EXPIRATION_DATE, true);
    }

    public static CreateCouponRequest withCode(String code) {
        return build(code, DEFAULT_DESCRIPTION, DEFAULT_DISCOUNT_VALUE, DEFAULT_EXPIRATION_DATE, false);
    }

    public static CreateCouponRequest withDiscountValue(BigDecimal discountValue) {
        return build(DEFAULT_CODE, DEFAULT_DESCRIPTION, discountValue, DEFAULT_EXPIRATION_DATE, false);
    }

    public static CreateCouponRequest withExpirationDate(LocalDate expirationDate) {
        return build(DEFAULT_CODE, DEFAULT_DESCRIPTION, DEFAULT_DISCOUNT_VALUE, expirationDate, false);
    }

    private static CreateCouponRequest build(String code, String description, BigDecimal discountValue,
                                              LocalDate expirationDate, boolean published) {
        return new CreateCouponRequest(code, description, discountValue, expirationDate, published);
    }
}
