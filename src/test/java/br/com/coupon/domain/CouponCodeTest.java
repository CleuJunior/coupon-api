package br.com.coupon.domain;

import br.com.coupon.domain.exception.InvalidCouponCodeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponCodeTest {

    @Test
    void shouldAcceptCodeWithExactlySixAlphanumericCharacters() {
        CouponCode code = CouponCode.of("ABC123");

        assertThat(code.getCode()).isEqualTo("ABC123");
    }

    @Test
    void shouldStripSpecialCharactersAndKeepSixCharacters() {
        CouponCode code = CouponCode.of("AB-12#34");

        assertThat(code.getCode()).isEqualTo("AB1234");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC12", "ABC", "A1", "AB-1#2"})
    void shouldRejectCodeShorterThanSixCharactersAfterSanitization(String rawCode) {
        assertThatThrownBy(() -> CouponCode.of(rawCode))
                .isInstanceOf(InvalidCouponCodeException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC12345", "A-B#C-1#2-3-4-5"})
    void shouldRejectCodeLongerThanSixCharactersAfterSanitization(String rawCode) {
        assertThatThrownBy(() -> CouponCode.of(rawCode))
                .isInstanceOf(InvalidCouponCodeException.class);
    }

    @Test
    void shouldBeEqualWhenUnderlyingCodeIsTheSame() {
        CouponCode first = CouponCode.of("ABC123");
        CouponCode second = CouponCode.of("ABC123");

        assertThat(first).isEqualTo(second).hasSameHashCodeAs(second);
    }
}