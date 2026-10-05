package br.com.coupon.application.usecase;

import br.com.coupon.application.port.CouponRepositoryPort;
import br.com.coupon.domain.Coupon;
import br.com.coupon.domain.exception.CouponAlreadyDeletedException;
import br.com.coupon.domain.exception.CouponNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseTest {

    @Mock
    private CouponRepositoryPort repositoryPort;
    @InjectMocks
    private DeleteCouponUseCase useCase;

    @Test
    void shouldSoftDeleteExistingCoupon() {
        Coupon coupon = Coupon.of("ABC123", "to delete", new BigDecimal("1.00"), LocalDate.now().plusDays(1), false);
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(coupon));

        useCase.execute(1L);

        assertThat(coupon.isDeleted()).isTrue();
        verify(repositoryPort).save(coupon);
    }

    @Test
    void shouldThrowWhenCouponDoesNotExist() {
        when(repositoryPort.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(CouponNotFoundException.class);

        verify(repositoryPort, never()).save(any());
    }

    @Test
    void shouldNotAllowDeletingAnAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.of("ABC123", "already deleted", new BigDecimal("1.00"), LocalDate.now().plusDays(1), false);
        coupon.delete();
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(coupon));

        assertThatThrownBy(() -> useCase.execute(1L))
                .isInstanceOf(CouponAlreadyDeletedException.class);

        verify(repositoryPort, never()).save(any());
    }
}