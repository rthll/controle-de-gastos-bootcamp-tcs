package com.controlegastos.investimentosservice.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.controlegastos.investimentosservice.exception.JwtValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token);
            return decodedJWT.getSubject();
        } catch (TokenExpiredException e) {
            throw new JwtValidationException("Token expirado.");
        } catch (SignatureVerificationException e) {
            throw new JwtValidationException("Assinatura do token inválida.");
        } catch (AlgorithmMismatchException e) {
            throw new JwtValidationException("Algoritmo de token inválido.");
        } catch (JWTDecodeException e) {
            throw new JwtValidationException("Erro ao decodificar o token.");
        } catch (JWTVerificationException e) {
            throw new JwtValidationException("Token inválido.");
        }
    }

    public String extractEmailFromToken(String token) {
        return validateToken(token);
    }
}
