package br.com.coupon.infra.persistence.mapper;

import br.com.coupon.domain.Coupon;
import br.com.coupon.infra.persistence.entities.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

    public CouponEntity toEntity(Coupon coupon) {
         CouponEntity couponEntity = new CouponEntity();

         couponEntity.setId(coupon.getId());
         couponEntity.setCode(coupon.getCode().getCode());
         couponEntity.setDescription(coupon.getDescription());
         couponEntity.setDiscountValue(coupon.getDiscountValue());
         couponEntity.setExpirationDate(coupon.getExpirationDate());
         couponEntity.setPublished(coupon.isPublished());
         couponEntity.setDeletedAt(coupon.getDeletedAt());

         return couponEntity;
    }

    public Coupon toDomain(CouponEntity couponEntity) {
        return Coupon.reconstruct(
                couponEntity.getId(),
                couponEntity.getCode(),
                couponEntity.getDescription(),
                couponEntity.getDiscountValue(),
                couponEntity.getExpirationDate(),
                couponEntity.isPublished(),
                couponEntity.getDeletedAt()
        );
    }

}
