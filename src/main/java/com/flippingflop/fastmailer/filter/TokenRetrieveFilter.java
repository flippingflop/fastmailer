package com.flippingflop.fastmailer.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TokenRetrieveFilter extends OncePerRequestFilter {

    private final String authToken;

    public TokenRetrieveFilter(String authToken) {
        this.authToken = authToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        Object authorization = request.getHeader("Authorization");
        boolean isAuthorized = authorization != null && ObjectUtils.nullSafeEquals(authToken, authorization.toString());

        if (isAuthorized) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            Authentication authentication = new PreAuthenticatedAuthenticationToken(null, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
