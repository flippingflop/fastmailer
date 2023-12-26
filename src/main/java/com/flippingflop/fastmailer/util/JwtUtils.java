package com.flippingflop.fastmailer.util;

import com.flippingflop.fastmailer.model.security.UserDetailsWithId;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Log4j2
@Component
public class JwtUtils implements InitializingBean {

    private static final long MINUTE = 1000 * 60;
    private static final long HOUR = 60 * MINUTE;

    @Value("${jwt.secret}")
    private String SECRET;
    private static SecretKey secretKey;

    @Override
    public void afterPropertiesSet() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
    }

    /**
     * Extract all claims from JWT
     * @param token
     * @return
     * @throws ExpiredJwtException - If token has expired.
     */
    public Claims extractClaims(String token) throws ExpiredJwtException {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
    }

    public Long getLoginUserId() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() == null) {
            return null;
        }
        UserDetailsWithId userDetailsWithId = (UserDetailsWithId) authentication.getPrincipal();
        return userDetailsWithId.getUserId();
    }

}
