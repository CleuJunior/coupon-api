package br.com.coupon.infra.web.controller;

import br.com.coupon.application.usecase.CreateCouponUseCase;
import br.com.coupon.application.usecase.DeleteCouponUseCase;
import br.com.coupon.domain.Coupon;
import br.com.coupon.infra.web.dto.CouponResponse;
import br.com.coupon.infra.web.dto.CreateCouponRequest;
import br.com.coupon.infra.web.mapper.CouponWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CreateCouponUseCase createCouponUseCase;
    private final DeleteCouponUseCase deleteCouponUseCase;
    private final CouponWebMapper mapper;

    @PostMapping
    @ResponseStatus(CREATED)
    public CouponResponse create(@RequestBody @Valid CreateCouponRequest request) {
        Coupon coupon = createCouponUseCase.execute(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                request.published()
        );

        return mapper.toCouponResponse(coupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        deleteCouponUseCase.execute(id);
    }
}
