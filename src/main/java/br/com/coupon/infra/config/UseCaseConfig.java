package br.com.coupon.infra.config;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.application.usecase.CreateCouponUseCase;
import br.com.coupon.application.usecase.DeleteCouponUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateCouponUseCase createCouponUseCase(CouponRepositoryPort couponRepositoryPort) {
        return new CreateCouponUseCase(couponRepositoryPort);
    }

    @Bean
    public DeleteCouponUseCase  deleteCouponUseCase(CouponRepositoryPort couponRepositoryPort) {
        return new DeleteCouponUseCase(couponRepositoryPort);
    }
}
