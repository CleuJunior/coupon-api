package br.com.coupon.infra.web.controller;

import br.com.coupon.fixture.CouponRequestFixture;
import br.com.coupon.infra.web.dto.CreateCouponRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CouponControllerIntegrationTest {

    private static final String COUPON_PATH = "/api/v1/coupon";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldCreateCouponAndReturnSanitizedCode() {
        CreateCouponRequest request = CouponRequestFixture.withCode("AB-12#34");

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(COUPON_PATH)
        .then()
                .statusCode(201)
                .body("code", is("AB1234"))
                .body("published", is(false));
    }

    @Test
    void shouldRejectCreationWithMissingRequiredField() {
        CreateCouponRequest request = CouponRequestFixture.withCode(null);

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(COUPON_PATH)
        .then()
                .statusCode(400)
                .body("status", is(400));
    }

    @Test
    void shouldRejectCreationWithDiscountBelowMinimum() {
        CreateCouponRequest request = CouponRequestFixture.withDiscountValue(new BigDecimal("0.1"));

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(COUPON_PATH)
        .then()
                .statusCode(400)
                .body("status", is(400))
                .body("message", containsString("Discount value"));
    }

    @Test
    void shouldRejectCreationWithExpirationDateInThePast() {
        CreateCouponRequest request = CouponRequestFixture.withExpirationDate(LocalDate.now().minusDays(1));

        given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(COUPON_PATH)
        .then()
                .statusCode(400)
                .body("message", containsString("Expiration date"));
    }

    @Test
    void shouldDeleteExistingCoupon() {
        long id = createCoupon(CouponRequestFixture.valid());

        given()
        .when()
                .delete(COUPON_PATH + "/{id}", id)
        .then()
                .statusCode(204);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingUnknownCoupon() {
        given()
        .when()
                .delete(COUPON_PATH + "/{id}", 999_999L)
        .then()
                .statusCode(404)
                .body("status", is(404));
    }

    @Test
    void shouldReturnConflictWhenDeletingAlreadyDeletedCoupon() {
        long id = createCoupon(CouponRequestFixture.valid());

        given()
        .when()
                .delete(COUPON_PATH + "/{id}", id)
        .then()
                .statusCode(204);

        given()
        .when()
                .delete(COUPON_PATH + "/{id}", id)
        .then()
                .statusCode(409)
                .body("status", is(409));
    }

    private long createCoupon(CreateCouponRequest request) {
        Number id = given()
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post(COUPON_PATH)
        .then()
                .statusCode(201)
                .extract()
                .path("id");

        return id.longValue();
    }
}
