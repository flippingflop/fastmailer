package com.flippingflop.fastmailer.filter;

import com.flippingflop.fastmailer.model.security.UserDetailsWithId;
import com.flippingflop.fastmailer.util.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Value("${auth.rest.token}")
    String authToken;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        SecurityContextHolder.clearContext();

        Object authorization = request.getHeader("Authorization");
        boolean tokenExists = authorization != null;
        /* Store authentication into SecurityContext by token type. */
        if (tokenExists) {
            String token = authorization.toString();
            boolean isBearerPrefix = token.startsWith("Bearer");

            // If token starts with "Bearer", it's JWT. Otherwise, it is presigned token.
            if (isBearerPrefix) {
                doJwtAuthentication(token);
            } else {
                doPresignedTokenAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void doJwtAuthentication(String token) {
        Claims claims = jwtUtils.extractClaims(token);
        List<String> authoritiesSet = (ArrayList<String>) claims.get("authorities");
        Collection<GrantedAuthority> authorities = authoritiesSet.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        Long userId = Long.parseLong(claims.getSubject());
        UserDetails userDetails = new UserDetailsWithId(claims.get("username", String.class), "N/A", authorities, userId);
        Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void doPresignedTokenAuthentication(String token) {
        if (token.equals(authToken)) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            Authentication authentication = new PreAuthenticatedAuthenticationToken(null, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
