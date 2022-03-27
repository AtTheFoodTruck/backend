//package com.sesac.domain.security;
//
//import com.sesac.domain.user.service.UserService;
//import com.sesac.jwt.JwtTokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//@RequiredArgsConstructor
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true) //메서드 단위로 @PreAuthorize 검증 어노테이션을 사용하기 위함
//public class WebSecurity extends WebSecurityConfigurerAdapter {
//
//    private final JwtTokenProvider tokenProvider;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//
//    /**
//     * Authorization 처리
//     * @author jaemin
//     * @version 1.0.0
//     * 작성일 2022-03-27
//    **/
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf()
//                .disable()
//                .authorizeRequests().antMatchers("/**")
//                .hasIpAddress("192.168.0.1")
//                .and()
//                .addFilter(getAuthenticationFilter());
//    }
//
//    /**
//     * 인증처리 필터, AuthenticationFilter extends Filter
//     * 요청에 대한 인증처리
//     * @author jaemin
//     * @version 1.0.0
//     * 작성일 2022-03-27
//    **/
//    private AuthenticationFilter getAuthenticationFilter() throws Exception {
//        AuthenticationFilter authenticationFilter
//                = new AuthenticationFilter(authenticationManager(), userService, env);
//        // 인증 매니저 등록
//        authenticationFilter.setAuthenticationManager(authenticationManager());
//
//        return authenticationFilter;
//    }
//
//    /**
//     * Authentication 처리
//     * email로 db조회 후 encrypted처리 후 비교
//     * @author jaemin
//     * @version 1.0.0
//     * 작성일 2022-03-27
//    **/
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        // 사용자의 email, password로 로그인처리
//        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//    }
//}
