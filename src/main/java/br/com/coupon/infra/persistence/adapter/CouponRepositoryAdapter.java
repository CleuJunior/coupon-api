package br.com.coupon.infra.persistence.adapter;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.domain.Coupon;
import br.com.coupon.infra.persistence.entities.CouponEntity;
import br.com.coupon.infra.persistence.mapper.CouponMapper;
import br.com.coupon.infra.persistence.repositories.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponRepositoryAdapter implements CouponRepositoryPort {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = this.mapper.toEntity(coupon);
        CouponEntity saved = this.repository.save(entity);

        return this.mapper.toDomain(saved);
    }

    @Override
    public Optional<Coupon> findById(Long id) {
        return this.repository.findById(id)
                .map(mapper::toDomain);
    }
}
