package com.sesac.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;

    private static final String AUTHORITIES_KEY = "auth";

    private static final long JWT_ACCESS_TOKEN_VALIDITY = 60 * 30; // 30 minutes
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7; // 1 week

    private Key key;

    /**
     * token 의 모든 claim 반환
     * @param token: jwt token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * token 으로부터 username, role map 반환
     * @param accessToken: jwt access token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public Map<String, Object> getUserParseInfo(String accessToken) {
        Claims claims = getAllClaimsFromToken(accessToken);
        Map<String, Object> result = new HashMap<>();
        result.put("email", claims.getSubject());
        result.put(AUTHORITIES_KEY, claims.get(AUTHORITIES_KEY));

        return result;
    }

    /**
     * Access Token 생성
     * @param authentication
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .claim(AUTHORITIES_KEY, authorities)  // authorities
                .setSubject(authentication.getName())  // email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    /**
     * Refresh Token 생성
     * @param authentication
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    public String generateRefreshToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())  // email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    /**
     * access token 에 담겨있는 권한 정보들(claims)을 이용해 Authentication 객체 리턴
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaimsFromToken(token);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
