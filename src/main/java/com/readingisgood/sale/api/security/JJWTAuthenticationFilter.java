package com.readingisgood.sale.api.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

public class JJWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JJWTAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getJwtFromAuthorizationHeader(request).flatMap(jwtUtils::validateAndDecode)
                .map(JwtHolder::getSubject)
                .ifPresentOrElse(JJWTAuthenticationFilter::authenticate, SecurityContextHolder::clearContext);
        filterChain.doFilter(request, response);
    }

    private static void authenticate(String subject) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private Optional<String> getJwtFromAuthorizationHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization")).map(value -> value.replace("Bearer ", ""));
    }
}
