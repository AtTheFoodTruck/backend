package com.sesac.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

public class JwtGenerator implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_ACCESS_TOKEN_VALIDITY = 15 * 60; //10
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 7; //일주일
    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public Map<String, Object> getUserParseInfo(String token) {
        Claims parseInfo = getAllClaimsFromToken(token);
        Map<String, Object> result = new HashMap<>();
        result.put("username", parseInfo.getSubject());
        result.put("role", parseInfo.get("role", List.class));

        return result;
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //check if the token has expired
    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    // Generate Access Token
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = new ArrayList<>();

        for (GrantedAuthority a: userDetails.getAuthorities()) {
            roles.add(a.getAuthority());
        }

        claims.put("role", roles);
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // Generate Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
