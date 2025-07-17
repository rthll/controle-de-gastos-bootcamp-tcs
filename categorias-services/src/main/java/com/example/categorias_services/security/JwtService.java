package com.example.categorias_services.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    private DecodedJWT getDecodedJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("login-auth-api")
                .build()
                .verify(token);
    }

    public String validateToken(String token) {
        try {
            return getDecodedJWT(token).getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String extractEmailFromToken(String token) {
        return validateToken(token); // mesmo que o "sub"
    }

    public String extractRoleFromToken(String token) {
        try {
            DecodedJWT jwt = getDecodedJWT(token);
            return jwt.getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }
}
