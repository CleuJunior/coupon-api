package br.com.coupon.application.usecase;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.domain.Coupon;
import br.com.coupon.domain.exception.InvalidDiscountValueException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort repositoryPort;
    @InjectMocks
    private CreateCouponUseCase useCase;

    @Test
    void shouldCreateAndPersistValidCoupon() {
        when(repositoryPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon result = useCase.execute("ABC123", "10% off", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        ArgumentCaptor<Coupon> captor = ArgumentCaptor.forClass(Coupon.class);
        verify(repositoryPort).save(captor.capture());
        assertThat(captor.getValue().getCode().getCode()).isEqualTo("ABC123");
        assertThat(result.getCode().getCode()).isEqualTo("ABC123");
    }

    @Test
    void shouldNotPersistWhenCouponDataIsInvalid() {
        assertThatThrownBy(() -> useCase.execute("ABC123", "invalid", new BigDecimal("0.1"), LocalDate.now().plusDays(1), false))
                .isInstanceOf(InvalidDiscountValueException.class);

        verify(repositoryPort, never()).save(any());
    }
}