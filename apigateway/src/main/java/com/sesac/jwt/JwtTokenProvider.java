package com.sesac.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements Serializable {
    private static final String AUTHORITIES_KEY = "auth";
    private static final long serialVersionUID = -2550185165626007488L;
    private static final long JWT_ACCESS_TOKEN_VALIDITY = 60 * 30; // 30 minutes
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7; // 1 week

    private Key key;

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
        //TODO 토큰 값 추출하는 것 수정, secret 대신 Key로 jwt토큰 생성 및 추출로 수정
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
        Map<String, Object> result = new HashMap<>();

        Claims parseInfo = getAllClaimsFromToken(accessToken);
        result.put("email", parseInfo.getSubject());
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
//    public String generateAccessToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        List<String> authorities = new ArrayList<>();
//
//        for (GrantedAuthority a: userDetails.getAuthorities()) {
//            authorities.add(a.getAuthority());
//        }
//
//        claims.put("role", authorities);
//        return Jwts.builder().setClaims(claims)
//                .setSubject(userDetails.get)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret).compact();
//    }

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
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token.getEmail(), token);
    }

    /**
     * AccessToken 생성
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    // Authentication 객체의 권한 정보를 이용해서 토큰을 생성
    public String createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + JWT_ACCESS_TOKEN_VALIDITY); //yml에 정의한 token 만료시간

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
}
