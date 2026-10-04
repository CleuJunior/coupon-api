package br.com.coupon.infra.web.mapper;

import br.com.coupon.domain.Coupon;
import br.com.coupon.infra.web.dto.CouponResponse;
import org.springframework.stereotype.Component;

@Component
public class CouponWebMapper {

    public CouponResponse toCouponResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue(),
                coupon.getExpirationDate(),
                coupon.isPublished()
        );
    }
}
