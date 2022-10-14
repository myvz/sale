package com.readingisgood.sale.api.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Optional;

public class JwtUtils {

    private final JWTVerifier jwtVerifier;

    public JwtUtils(JWTVerifier jwtVerifier) {
        this.jwtVerifier = jwtVerifier;
    }

    /**
     * Validate and decode Token
     * @param token
     * @return JwtHolder class instance if token is valid. Otherwise, returns an Empty Optional
     */
    public Optional<JwtHolder> validateAndDecode(String token) {
        try {
            return Optional.of(new JwtHolder(jwtVerifier.verify(token)));
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
