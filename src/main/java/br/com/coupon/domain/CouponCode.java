package br.com.coupon.domain;

import br.com.coupon.domain.exception.InvalidCouponCodeException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponCode {

    private static final int REQUIRED_LENGTH = 6;
    private final String code;

    public static CouponCode of(String code) {
        String standardizedCode = standardize(code);

        return new CouponCode(standardizedCode);
    }

    private static String standardize(String code) {
        String std = code.replaceAll("[^a-zA-Z0-9]", "");

        if (std.length() != REQUIRED_LENGTH) {
            throw new InvalidCouponCodeException(std);
        }

        return std;
    }
}
