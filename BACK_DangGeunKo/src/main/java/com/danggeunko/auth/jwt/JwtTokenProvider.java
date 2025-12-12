package com.danggeunko.auth.jwt;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

    // 데모용 시크릿 키 (실제 서비스에서는 환경변수 / 설정 파일 사용)
    private final String secret = "lets-run-with-danggeunko-secret-key";
    private final long accessTokenValidityInMs = 1000L * 60 * 30;            // 30분
    private final long refreshTokenValidityInMs = 1000L * 60 * 60 * 24 * 7; // 7일

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(int userId, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityInMs);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .claim("type", "ACCESS")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String createRefreshToken(int userId, String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .claim("role", role)
                .claim("type", "REFRESH")
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }
    
    public int getUserId(String token) {
    	 return (int) parseClaims(token).getBody().get("userId");
    }
    
    public String getUsername(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public String getRole(String token) {
        return (String) parseClaims(token).getBody().get("role");
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, "ACCESS");
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, "REFRESH");
    }

    private boolean validateToken(String token, String expectedType) {
        try {
            Jws<Claims> jws = parseClaims(token);
            String type = (String) jws.getBody().get("type");
            if (!expectedType.equals(type)) {
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + expectedType);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT token: " + expectedType);
        }
        return false;
    }

    public Instant getExpiry(String token) {
        return parseClaims(token).getBody().getExpiration().toInstant();
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token);
    }
}