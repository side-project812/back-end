package com.qeat.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class JwtTokenUtils {

    private static final String SECRET = "your-secret-key";

    public static Long extractUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public static long extractExpiration(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    private static Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
    }
}