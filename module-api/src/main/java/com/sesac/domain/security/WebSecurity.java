package com.sesac.domain.security;

import com.sesac.domain.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //메서드 단위로 @PreAuthorize 검증 어노테이션을 사용하기 위함
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authorization 처리
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //csrf 토큰 비활성화, 시큐리티에서 js로 요청이오면 csft토큰이 없어서 막아버리는데, disable로 해결
                .csrf().disable()

                // exception 핸들링
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 401
                .accessDeniedHandler(jwtAccessDeniedHandler) // 403 user -> adminPage access denied

                // h2-console 설정
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()

                // 세션을 사용하기 않기에 세션 설정을 STATELESS로 지정
                .and()
                    .logout().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(STATELESS)

                // request 처리
                .and()
                    .authorizeRequests()
                    .antMatchers("/actuator/**").permitAll()
                    .antMatchers("/**").permitAll()
//                    .anyRequest().permitAll()
//                    .antMatchers("/login").permitAll()
//                    .antMatchers("/managers/status").permitAll() // 사업자등록
                    .anyRequest().authenticated()

                // 로그인
//                .and()
//                    .formLogin()
//                    .loginPage("/login")

                // JwtSecurityConfig filter에 추가한걸 적용
                .and()
                    .apply(new JwtSecurityConfig(tokenProvider, redisTemplate));
    }
}