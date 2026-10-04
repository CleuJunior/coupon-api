package br.com.coupon.application.port;

import br.com.coupon.domain.Coupon;

import java.util.Optional;

public interface CouponRepositoryPort {

    Coupon save(Coupon coupon);
    Optional<Coupon> findById(Long id);
}