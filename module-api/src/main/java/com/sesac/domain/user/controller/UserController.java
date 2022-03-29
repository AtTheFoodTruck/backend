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

        // redis 에 저장
        redisService.setRefreshToken(requestUser.getEmail(), refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        // Header에 추가
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt토큰 return                           body            header          status
        return new ResponseEntity<>(new TokenDto(accessToken), httpHeaders, HttpStatus.OK);
    }

    /**
     * access token 갱신
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> updateRefreshToken(@Valid @RequestBody UpdateTokenDto tokenDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        // refresh validation 체크
        if (!jwtTokenProvider.validateToken(refreshToken)) {
//            TODO error 처리
        }

        // refresh token validation 통과 시, email 추출
        String email = (String) jwtTokenProvider.getUserParseInfo(refreshToken).get("email");


        // email 없는 경우
//        if (email == null) {
//            TODO error 처리
//            return new ResponseDto<>(new TokenDto(null), httpHeaders, HttpStatus.BAD_REQUEST);
//        }


        if( !StringUtils.hasText(email) ) {
            throw new IllegalArgumentException(email);
        }

        // redis 에서 refresh token 가져옴
        String refreshTokenFromDb = redisService.getRefreshToken(email);

        // redis 에 저장된 refresh token 이 요청으로 들어온 token 과 동일한지 체크
        if (!refreshToken.equals(refreshTokenFromDb)) {
            // 잘못된 refresh token 이거나, 시간이 만료돼서 redis 에 refresh token 이 없거나(null)
            // TODO error 처리
        }

        // refresh token 이 유효하고, redis 에 저장되어 있는 것과 동일한 경우
        // 새로운 access token 발급
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String newAccessToken = jwtTokenProvider.createToken(authentication, false);

        // 새로운 access token Headers 에 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + newAccessToken);

        // jwt 토큰 return                           body            header          status
        return new ResponseEntity<>(new TokenDto(newAccessToken), httpHeaders, HttpStatus.OK);
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
