package com.sesac.jwt;


import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Component
public class JwtRequestFilter extends AbstractGatewayFilterFactory<JwtRequestFilter.Config> {
    @Value("${jwt.secret}")
    private String secret;

    public JwtRequestFilter() {
        super(Config.class);
    }

    @Getter
    @AllArgsConstructor
    public static class Config {
        private String role;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (!containsAuthorization(request)) {
                return onError(exchange, "No authorization header", HttpStatus.BAD_REQUEST);
            }
            String token = extractToken(request);  // "Bearer " 이후 String
            log.info("JWT token: " + token);

            if (!isJwtValid(token)) {
                return onError(exchange, "Invalid authorization header", HttpStatus.BAD_REQUEST);
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

    private boolean isJwtValid(String token) {
        String subject = null;
        try {
            subject = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody()
                    .getSubject();
        } catch (NullPointerException e) {
            log.warn("NullPointerException");
            return false;
        } catch (SignatureException | ExpiredJwtException e) {
            log.warn("Signature of the token is wrong or Token is expired");
            return false;
        } // token is expired
        catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {  // format is wrong
            log.warn("The token is wrong format");
            return false;
        }
        if (subject.isEmpty()) {
            return false;
        }

        return true;
    }

    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).substring(7);
    }

}