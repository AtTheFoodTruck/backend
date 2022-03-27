package com.sesac.domain.user.controller;

import com.sesac.domain.common.ResponseDto;
import com.sesac.domain.common.TokenDto;
import com.sesac.domain.user.dto.RequestUser;
import com.sesac.domain.user.dto.ResponseUser;
import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.service.UserService;
import com.sesac.jwt.JwtTokenProvider;
import com.sesac.redis.RedisService;
import com.sesac.redis.RedisToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;

    /**
     * 개인 회원 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-26
    **/
    @PostMapping("/users/join")
    public ResponseDto join(@Valid @RequestBody RequestUser member, BindingResult result) {
        // validation 검증
        if (result.hasErrors()) {
            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getFieldError());
        }

        User joinUser = userService.join(member);

        ResponseUser responseUser = ResponseUser.builder()
                .username(joinUser.getUsername())
                .email(joinUser.getEmail())
                .phoneNum(joinUser.getPhoneNum())
                .build();

        return new ResponseDto(HttpStatus.CREATED.value(), responseUser);
    }

    /**
     * 로그인
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    // 로그인
    @PostMapping("/users/login")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody RequestUser requestUser, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword());

        // loadUserByUsername메서드에서 리턴받은 user객체로 Authentication객체 생성
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 생성된 객체를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 생성된 객체로 TokenProvider.createToken 메서드를 통해 jwt토큰을 생성
//        String jwt = jwtTokenProvider.createToken(authentication);
        String accessToken = jwtTokenProvider.createToken(authentication, false);
        String refreshToken = jwtTokenProvider.createToken(authentication, true);

        // redis 에 저장
        redisService.setRefreshToken(new RedisToken(requestUser.getEmail(), refreshToken));

        HttpHeaders httpHeaders = new HttpHeaders();
        // Header에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt토큰 return                           body            header          status
        return new ResponseEntity<>(new TokenDto(accessToken), httpHeaders, HttpStatus.OK);
    }


}
