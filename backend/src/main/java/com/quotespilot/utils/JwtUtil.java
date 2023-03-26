package com.quotespilot.utils;

import com.quotespilot.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.token.expiration}")
    private int expireInMs;
    
    private final static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generate(User user) {
        return Jwts.builder()
                .setSubject(user.getName())
                .claim("role",user.getRole())
                .setIssuer("quote-pilot")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireInMs))
                .signWith(key)
                .compact();
    }
    public boolean validate(String token) {
        if (getUsername(token) != null && isExpired(token)) {
            return true;
        }
        return false;
    }

    public String getUsername(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    public String getRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role").toString();
    }

    public boolean isExpired(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }
}
