package com.sesac.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenProvider implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    private static final String REFRESH_TOKEN_KEY = "RF";
    private static final long JWT_ACCESS_TOKEN_VALIDITY = 60 * 30; // 30 minutes
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7; // 1 week

    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * token 의 모든 claim 반환
     * @param token: jwt token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * token 에서 원하는 claim 반환
     * @param token: jwt token
     * @param claimsResolver: token 에서 얻고자하는 claim method
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * token 에서 username 반환
     * @param accessToken: jwt access token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public String getUsernameFromToken(String accessToken) {
        return getClaimFromToken(accessToken, claims -> claims.getSubject());
    }

    /**
     * token 으로부터 username, role map 반환
     * @param accessToken: jwt access token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public Map<String, Object> getUserParseInfo(String accessToken) {
        Claims parseInfo = getAllClaimsFromToken(accessToken);
        Map<String, Object> result = new HashMap<>();
        result.put("username", parseInfo.getSubject());
        result.put("role", parseInfo.get("role", List.class));

        return result;
    }

    /**
     * token 만료 여부
     * @param token: jwt token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    public Boolean isTokenExpired(String token) {
        return getClaimFromToken(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Access Token 생성
     * @param userDetails
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> authorities = new ArrayList<>();

        for (GrantedAuthority a: userDetails.getAuthorities()) {
            authorities.add(a.getAuthority());
        }

        claims.put("role", authorities);
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Refresh Token 생성
     * @param username
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    public String generateRefreshToken(String username) {
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    /**
     * Redis 에 Refresh Token 저장
     * @param token: username(key), refreshToken(value)
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    private void saveRefreshToken(Token token) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token.getUsername(), token.getRefreshToken());
    }
}
