package br.com.coupon.infra.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "coupon.openapi")
public record OpenApiProperties(String title, String description, String version) {
}