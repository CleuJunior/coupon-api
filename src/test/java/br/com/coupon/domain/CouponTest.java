package br.com.coupon.domain;

import br.com.coupon.domain.exception.CouponAlreadyDeletedException;
import br.com.coupon.domain.exception.InvalidDiscountValueException;
import br.com.coupon.domain.exception.PastExpirationDateException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    @Test
    void shouldCreateCouponWithValidData() {
        Coupon coupon = Coupon.of("ABC123", "10% off", new BigDecimal("10.00"), LocalDate.now().plusDays(1), false);

        assertThat(coupon.getCode().getCode()).isEqualTo("ABC123");
        assertThat(coupon.isPublished()).isFalse();
        assertThat(coupon.isDeleted()).isFalse();
    }

    @Test
    void shouldAllowCreatingAlreadyPublishedCoupon() {
        Coupon coupon = Coupon.of("ABC123", "10% off", new BigDecimal("10.00"), LocalDate.now().plusDays(1), true);

        assertThat(coupon.isPublished()).isTrue();
    }

    @Test
    void shouldAcceptMinimumDiscountValueOfZeroPointFive() {
        Coupon coupon = Coupon.of("ABC123", "min discount", new BigDecimal("0.5"), LocalDate.now().plusDays(1), false);

        assertThat(coupon.getDiscountValue()).isEqualByComparingTo("0.5");
    }

    @Test
    void shouldRejectDiscountValueBelowMinimum() {
        assertThatThrownBy(() -> Coupon.of("ABC123", "too low", new BigDecimal("0.49"), LocalDate.now().plusDays(1), false))
                .isInstanceOf(InvalidDiscountValueException.class);
    }

    @Test
    void shouldAcceptExpirationDateToday() {
        Coupon coupon = Coupon.of("ABC123", "expires today", new BigDecimal("1.00"), LocalDate.now(), false);

        assertThat(coupon.getExpirationDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldRejectExpirationDateInThePast() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        assertThatThrownBy(() -> Coupon.of("ABC123", "expired", new BigDecimal("1.00"), yesterday, false))
                .isInstanceOf(PastExpirationDateException.class);
    }

    @Test
    void shouldSoftDeleteCoupon() {
        Coupon coupon = Coupon.of("ABC123", "to delete", new BigDecimal("1.00"), LocalDate.now().plusDays(1), false);

        coupon.delete();

        assertThat(coupon.isDeleted()).isTrue();
        assertThat(coupon.getDeletedAt()).isNotNull();
    }

    @Test
    void shouldNotAllowDeletingAnAlreadyDeletedCoupon() {
        Coupon coupon = Coupon.of("ABC123", "to delete", new BigDecimal("1.00"), LocalDate.now().plusDays(1), false);
        coupon.delete();

        assertThatThrownBy(coupon::delete)
                .isInstanceOf(CouponAlreadyDeletedException.class);
    }

    @Test
    void shouldReconstructCouponPreservingIdAndDeletedAt() {
        LocalDateTime deletedAt = LocalDateTime.now().minusDays(1);

        Coupon coupon = Coupon.reconstruct(1L, "ABC123", "reconstructed", new BigDecimal("1.00"),
                LocalDate.now().plusDays(1), false, deletedAt);

        assertThat(coupon.getId()).isEqualTo(1L);
        assertThat(coupon.getDeletedAt()).isEqualTo(deletedAt);
        assertThat(coupon.isDeleted()).isTrue();
    }
}