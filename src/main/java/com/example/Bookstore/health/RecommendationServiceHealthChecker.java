package com.example.Bookstore.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RecommendationServiceHealthChecker implements HealthIndicator {

    @Override
    public Health health() {
        try {
            final var restTemplate = new RestTemplate();
            final var recommendedServiceUrl = "http://localhost:8080/api/recommend/healthcheck";
            return restTemplate.getForEntity(recommendedServiceUrl, String.class).getStatusCode().is2xxSuccessful()
                    && "OK".equals(restTemplate.getForObject(recommendedServiceUrl, String.class))
                    ? Health.up().withDetail("Recommendation Service", "Available").build()
                    : Health.down().withDetail("Recommendation Service", "Unhealthy response").build();
        } catch (final Exception e) {
            return Health.down(e).withDetail("Recommendation Service", "Not reachable").withDetail("error", e.getMessage()).build();
        }
    }
}
