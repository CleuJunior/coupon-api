package br.com.coupon.application.usecase;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.domain.Coupon;
import br.com.coupon.domain.exception.CouponNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponRepositoryPort repositoryPort;

    public void execute(Long id) {
        Coupon coupon = this.repositoryPort.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(id));

        coupon.delete();

        repositoryPort.save(coupon);
    }
}
