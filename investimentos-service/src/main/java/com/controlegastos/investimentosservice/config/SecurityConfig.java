package com.controlegastos.investimentosservice.config;

import com.controlegastos.investimentosservice.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/investimentos/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/investimentos/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/investimentos/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/investimentos/**").authenticated()
                        //adicionei novas rotas
                        .requestMatchers(HttpMethod.GET, "/compras/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/compras/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/compras/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/compras/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/rendas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/rendas/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/rendas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/rendas/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}