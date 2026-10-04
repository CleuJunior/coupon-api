package br.com.coupon.infra.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CouponResponse(
        Long id,
        String code,
        String description,
        BigDecimal discountValue,
        LocalDate expirationDate,
        boolean published) {
}
