package com.readingisgood.sale.api.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JwtHolder {

    private final DecodedJWT decodedJWT;

    public JwtHolder(DecodedJWT decodedJWT) {
        this.decodedJWT = decodedJWT;
    }

    public String getIssuer() {
        return decodedJWT.getIssuer();
    }

    public String getSubject() {
        return decodedJWT.getSubject();
    }

    public List<String> getAudience() {
        return decodedJWT.getAudience();
    }

    public Date getExpiresAt() {
        return decodedJWT.getExpiresAt();
    }

    public Instant getExpiresAtAsInstant() {
        return decodedJWT.getExpiresAtAsInstant();
    }

    public Date getNotBefore() {
        return decodedJWT.getNotBefore();
    }

    public Instant getNotBeforeAsInstant() {
        return decodedJWT.getNotBeforeAsInstant();
    }

    public Date getIssuedAt() {
        return decodedJWT.getIssuedAt();
    }

    public Instant getIssuedAtAsInstant() {
        return decodedJWT.getIssuedAtAsInstant();
    }

    public String getId() {
        return decodedJWT.getId();
    }

    public Claim getClaim(String name) {
        return decodedJWT.getClaim(name);
    }

    public Map<String, Claim> getClaims() {
        return decodedJWT.getClaims();
    }
}
