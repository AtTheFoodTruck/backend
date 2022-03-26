package com.sesac.config;

import com.sesac.jwt.JwtAuthenticationGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public RouteLocator gatewayRouteLocator(RouteLocatorBuilder builder, JwtAuthenticationGatewayFilterFactory jwtAuthenticationGatewayFilterFactory) {
        return builder.routes()
                .route("login_route", r -> r.path("/auth/login")
                        .filters(f -> f.filter(jwtAuthenticationGatewayFilterFactory.apply(new JwtAuthenticationGatewayFilterFactory.Config("ROLE_USER"))).addRequestHeader("Hello", "World")
                                .rewritePath("/new", "/"))
                        .uri("http://localhost:/"))
                .build();
    }

}
