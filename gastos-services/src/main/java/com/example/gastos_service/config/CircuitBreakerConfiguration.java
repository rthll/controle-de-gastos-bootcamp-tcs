package com.example.gastos_service.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 50% de falhas para abrir o circuito
                .waitDurationInOpenState(Duration.ofSeconds(30)) // Aguarda 30s antes de tentar novamente
                .slidingWindowSize(10) // Considera as últimas 10 chamadas
                .minimumNumberOfCalls(5) // Mínimo de 5 chamadas para calcular a taxa de falha
                .slowCallRateThreshold(50) // 50% de chamadas lentas
                .slowCallDurationThreshold(Duration.ofSeconds(2)) // Chamadas > 2s são consideradas lentas
                .permittedNumberOfCallsInHalfOpenState(3) // 3 chamadas no estado meio-aberto
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker categoriaServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("categoria-service");
    }
}