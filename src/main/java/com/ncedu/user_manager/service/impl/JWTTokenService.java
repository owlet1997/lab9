package com.ncedu.user_manager.service.impl;

import com.ncedu.user_manager.model.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTTokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetimeMilliseconds}")
    private long lifetime;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean isTokenExpired(Date createdWhen) {
        return createdWhen == null || createdWhen.before(new Date(System.currentTimeMillis() - lifetime));
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userPrincipal.getUsername(), userPrincipal.getAccessTokenId());
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userPrincipal.getUsername(), userPrincipal.getRefreshTokenId());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, String id) {
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifetime))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validateAccessToken(String token, UserPrincipal userPrincipal) {
        final String username = getUsernameFromToken(token);
        final String id = getIdFromToken(token);
        return username.equals(userPrincipal.getUsername()) &&
                id.equals(userPrincipal.getAccessTokenId()) &&
                !isTokenExpired(token);
    }

    public Boolean validateRefreshToken(String token, UserPrincipal userPrincipal) {
        final String username = getUsernameFromToken(token);
        final String id = getIdFromToken(token);
        return username.equals(userPrincipal.getUsername()) &&
                id.equals(userPrincipal.getRefreshTokenId());
                // && !isTokenExpired(token); We would have infinite refresh tokens
    }

}
