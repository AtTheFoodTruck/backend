package com.sesac.domain.user.controller;

import com.sesac.domain.common.TokenDto;
import com.sesac.domain.common.UpdateTokenDto;
import com.sesac.domain.user.dto.*;
import com.sesac.domain.user.dto.request.*;
import com.sesac.domain.user.service.UserService;
import com.sesac.domain.jwt.JwtTokenProvider;
import com.sesac.domain.redis.RedisService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final Response response;
    private final Environment env;


    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/health_check")
    @Timed(value="users.status", longTask = true)
    public String status() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", message=" + env.getProperty("greeting.message")
                + ", token secret=" + env.getProperty("jwt.secret"));
    }

    /**
     * ?????? ????????????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-26
    **/
    @PostMapping("/users/join")
    public ResponseEntity<?> signUpUser(@Valid @RequestBody UserRequestDto.JoinUserDto userDto, BindingResult results) {

        log.info("?????? ????????????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.signUpUser(userDto);
    }

    /**
     * ?????? ????????????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-29
    **/
    @PostMapping("/managers/join")
    public ResponseEntity<?> signUpManager(@Valid @RequestBody UserRequestDto.JoinManagerDto managerDto, BindingResult results) {

        log.info("?????? ????????????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.signUpManager(managerDto);
    }

    /**
     * ?????????
     * ?????????, ??????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-27
    **/
    // ?????????
    @PostMapping("/logins")
    public ResponseEntity<?> authorize(@RequestBody UserRequestDto.LoginUserDto requestUser) {

        log.info("????????? request");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(requestUser.getEmail(), requestUser.getPassword());

        // loadUserByUsername??????????????? ???????????? user????????? Authentication?????? ??????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // ????????? ????????? SecurityContext??? ??????
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ????????? ????????? TokenProvider.createToken ???????????? ?????? jwt????????? ??????
        String accessToken = jwtTokenProvider.createToken(authentication, false);
        String refreshToken = jwtTokenProvider.createToken(authentication, true);

        // redis??? ??????
        redisService.setRefreshToken(requestUser.getEmail(), refreshToken);

        HttpHeaders httpHeaders = new HttpHeaders();

        // Header??? ??????
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // jwt??????
        return response.successToken(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);
//        return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }

    /**
     * ????????????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-29
    **/
    @PostMapping("/users/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody UserRequestDto.LogoutUserDto logoutDto, BindingResult results) {

        log.info("????????????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.logout(logoutDto);
    }

    /**
     * access token ??????
     * ?????? ?????? ??????, ???????????? ?????? ?????? - jaemin
     * @author jjaen
     * @version 1.0.0
     * ????????? 2022-03-29
    **/
    @PostMapping("/users/refresh")
    public ResponseEntity<?> updateRefreshToken(@Valid @RequestBody UpdateTokenDto updateTokenDto, BindingResult results) {

        log.info("Access Token ??????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updateRefreshToken(updateTokenDto);
    }

    /**
     * ?????? ?????? ??????(????????? ??????)
     * ?????????, ??????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-28
     **/
    @PatchMapping("/name")
    public ResponseEntity<?> updateUsername(Principal principal,
                                      @Valid @RequestBody UserRequestDto.UpdateNameDto updateNameDto,
                                      BindingResult results) {
        log.info("?????????????????? - ?????????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updateUsername(principal.getName(), updateNameDto);
    }

    /**
     * ?????? ?????? ??????(???????????? ??????)
     * ?????????, ??????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-29
    **/
    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(Principal principal,
                                      @Valid @RequestBody UserRequestDto.UpdatePwDto updatePwDto,
                                      BindingResult results) {
        log.info("?????????????????? - ???????????? ??????");

        // validation ??????
        if (results.hasErrors()) {
            return response.invalidFields(Helper.refineErrors(results));
        }

        return userService.updatePassword(principal.getName(), updatePwDto);
    }

    /**
     * ????????? ?????? ??????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-29
     **/
    @PostMapping("/validation/email")
    public ResponseEntity<?> validateDuplicateEmail(@RequestBody UserDto userDto) {

        return userService.validateDuplicateEmail(userDto.getEmail());
//        return new ResponseDto(HttpStatus.OK.value(), "????????? ?????? ?????? ??????");
    }

    /**
     * ????????? ?????? ??????
     * @author jaemin
     * @version 1.0.0
     * ????????? 2022-03-29
    **/
    @PostMapping("/validation/name")
    public ResponseEntity<?> validateDuplicateUsername(@RequestBody UserDto userDto) {
        return userService.validateDuplicateUser(userDto.getUsername());
    }
}
