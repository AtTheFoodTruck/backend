package com.sesac.domain.user.controller;

import com.sesac.domain.common.ResponseDto;
import com.sesac.domain.common.TokenDto;
import com.sesac.domain.common.UpdateTokenDto;
import com.sesac.domain.user.dto.*;
import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.service.UserService;
import com.sesac.domain.jwt.JwtTokenProvider;
import com.sesac.domain.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;


    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    /**
     * 개인 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-26
    **/
    @PostMapping("/users/join")
    public ResponseDto signUpUser(@Valid @RequestBody RequestUserDto userDto, BindingResult result) {

        // validation 검증
        String errorMessage = result.getFieldErrors().stream()
                .map(e -> e.getField())
                .collect(Collectors.joining(","));

        if (StringUtils.hasText(errorMessage)) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage);
        }

        User joinUser = userService.signUpUser(userDto);

        return new ResponseDto(HttpStatus.CREATED.value(), new ResponseUserDto(joinUser));
    }
    
    /**
     * 점주 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/managers/join")
    public ResponseDto signUpManager(@Valid @RequestBody RequestManagerDto managerDto, BindingResult result) {

        // validation 검증
        String errorMessage = result.getFieldErrors().stream()
                .map(e -> e.getField())
                .collect(Collectors.joining(","));

        if (StringUtils.hasText(errorMessage)) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage);
        }

        User joinManager = userService.signUpManager(managerDto);

        return new ResponseDto(HttpStatus.CREATED.value(), new ResponseUserDto(joinManager));
    }

    /**
     * 로그인
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    // 로그인
    @PostMapping("/users/login")
    public ResponseEntity<TokenDto> authorize(@RequestBody LoginUserDto requestUser) {

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

        // redis에 저장
        redisService.setRefreshToken(requestUser.getEmail(), refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        // Header에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt토큰 return                           body            header          status
        return new ResponseEntity<>(new TokenDto(accessToken), httpHeaders, HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/logout")
    public ResponseDto logout(@Valid @RequestBody LogoutUserDto logoutDto, BindingResult result) {
        // validation 검증
        String errorMessage = result.getFieldErrors().stream()
                .map(e -> e.getField())
                .collect(Collectors.joining(","));

        if (StringUtils.hasText(errorMessage)) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), errorMessage);
        }

        return new ResponseDto(HttpStatus.OK.value(), userService.logout(logoutDto));
    }

    /**
     * access token 갱신
     * 검증 로직 추가, 서비스로 로직 분리 - jaemin
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/refresh")
    public ResponseEntity updateRefreshToken(@Valid @RequestBody UpdateTokenDto updateTokenDto, BindingResult result) {

        // validation 검증
        String errorMessage = result.getFieldErrors().stream()
                .map(e -> e.getField())
                .collect(Collectors.joining(","));

        if (StringUtils.hasText(errorMessage)) {
//            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST.value());
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return userService.updateRefreshToken(updateTokenDto);

    }

    /**
     * 회원 정보 수정(닉네임 변경)
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
     **/
    @PatchMapping("/name")
    public ResponseDto updateUsername(Principal principal,
                                      @Valid @RequestBody UpdateNameDto updateNameDto,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), result.getFieldError());
        }

        User updatedInfo = userService.updateUsername(principal.getName(), updateNameDto);

        return new ResponseDto(HttpStatus.OK.value(), updatedInfo);

    }

    /**
     * 회원 정보 수정(비밀번호 변경)
     * 사용자, 점주
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PatchMapping("/password")
    public ResponseDto updatePassword(Principal principal,
                                      @Valid @RequestBody UpdatePwDto updatePwDto,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), result.getFieldError());
        }

        userService.updatePassword(principal.getName(), updatePwDto);

        return new ResponseDto(HttpStatus.OK.value(), "비밀번호 수정 성공");
    }

    /**
     * 이메일 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @PostMapping("/validation/email")
    public ResponseDto validateDuplicateEmail(@RequestBody UserDto userDto) {

        userService.validateDuplicateEmail(userDto.getEmail());
        return new ResponseDto(HttpStatus.OK.value(), "이메일 중복 체크 성공");
    }

    /**
     * 닉네임 중복 체크
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/validation/name")
    public ResponseDto validateDuplicateUsername(@RequestBody UserDto userDto) {
        userService.validateDuplicateUser(userDto.getUsername());
        return new ResponseDto(HttpStatus.OK.value(), "닉네임 중복 체크 성공");
    }

}
