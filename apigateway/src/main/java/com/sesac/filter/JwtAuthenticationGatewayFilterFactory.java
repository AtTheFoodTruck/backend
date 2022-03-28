package com.sesac.filter;


import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Order(-2)
@Component
public class JwtAuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private TokenProvider tokenProvider;
    private Key key;

    public JwtAuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Getter
    @AllArgsConstructor
    public static class Config {
        private String role;
    }


    /**
     * Jwt token 인증 filter
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // request header 에 Authorization 가 없는 경우
            if (!containsAuthorization(request)) {
                return onError(exchange, "No Authorization header", HttpStatus.BAD_REQUEST);
            }

            // 토큰 value 추출
            String token = extractToken(request);  // "Bearer " 이후 String
            log.info("JWT token: " + token);

            // JWT token 이 유효한지 확인
            if (!tokenProvider.validateToken(token)) {
                return onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // 해당 request 가 허용된 권한이 JWT token 의 user role 을 포함하고 있는지 확인
            if (! hasRole(config, token)) {
                return onError(exchange, "Invalid role", HttpStatus.UNAUTHORIZED);
            }

            // jwt token 인증 성공
            return chain.filter(exchange);
        };
    }

    /**
     * response 에 error status 추가
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

    /**
     * header 안 Authentication 포함 여부
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    private boolean containsAuthorization(ServerHttpRequest request) {
        return request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    /**
     * header 의 Authentication 에서 token 추출
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
     **/
    private String extractToken(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION).get(0).substring(7);
    }

    /**
     * role 체크
     * - filter 로 들어온 request 에 필요한 role 을 유저가 갖고 있는지
     * @param config: 해당 request 에 필요한 role
     * @param token: jwt access token
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/27
    **/
    private boolean hasRole(Config config, String token) {
        log.info("hasRole token: " + token);
        ArrayList<String> authorities = (ArrayList<String>) tokenProvider.getUserParseInfo(token).get("role");
        log.info("role of request user: " + authorities);

        if (! authorities.contains(config.getRole())) {
            return false;
        }
        return true;
    }

}