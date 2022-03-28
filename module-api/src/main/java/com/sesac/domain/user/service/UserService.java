package com.sesac.domain.user.service;

import antlr.StringUtils;
import com.sesac.domain.user.dto.RequestUserDto;
import com.sesac.domain.user.dto.UpdateUserDto;
import com.sesac.domain.user.entity.Authority;
import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.repository.UserRepository;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 개인 회원 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
    **/
    @Transactional
    public User join(RequestUserDto user) {
        if (userRepository.findByUsername(user.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // param으로 받은 user로 권한정보 생성, 가입은 USER로만
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User createdUser = User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return userRepository.save(createdUser);
    }

    /**
     * 회원 정보 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
     * @param email
    **/
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다"));
    }

    /**
     * 사용자 정보 수정 [유저, 매니저]
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
    **/
    @Transactional
    public User updateUsername(String email, UpdateUserDto updateUserDto) {
        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(닉네임)
        if (!StringUtil.isNullOrEmpty(updateUserDto.getUsername())) {
            user.changeUser(updateUserDto.getUsername());
        }
        return user;
    }

    @Transactional
    public void updatePassword(String email, UpdateUserDto updateUserDto) {
        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(비밀번호)
        if (!StringUtil.isNullOrEmpty(updateUserDto.getCurrentPassword())) {
            if (!passwordEncoder.matches(updateUserDto.getCurrentPassword(), user.getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
        }
        user.encodingPassword(passwordEncoder.encode(updateUserDto.getNewPassword()));
    }

}
