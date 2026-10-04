package br.com.coupon.application.usecase;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.domain.Coupon;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
public class CreateCouponUseCase {

    private final CouponRepositoryPort repositoryPort;

    public Coupon execute(String code, String description, BigDecimal discountValue, LocalDate expirationDate, boolean published) {
        Coupon coupon = Coupon.of(code, description, discountValue, expirationDate, published);
        return this.repositoryPort.save(coupon);
    }
}
