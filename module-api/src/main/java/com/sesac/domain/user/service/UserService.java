package com.sesac.domain.user.service;

import com.sesac.domain.exception.DuplicateUsernameException;
import com.sesac.domain.user.dto.RequestManagerDto;
import com.sesac.domain.user.dto.RequestUserDto;
import com.sesac.domain.user.dto.UpdatePwDto;
import com.sesac.domain.user.dto.UpdateNameDto;
import com.sesac.domain.user.entity.Authority;
import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public User signUpUser(RequestUserDto user) {
        // 중복회원 검증
        validateDuplicateUser(user.getEmail());

        if (userRepository.findByUsername(user.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        // User 권한 생성
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

    // TODO 점주에는 가게 정보까지 저장?
    /**
     * 점주 회원가입
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    @Transactional
    public User signUpManager(RequestManagerDto manager) {
        // 중복 체크
        validateDuplicateUser(manager.getUsername());
        validateDuplicateEmail(manager.getEmail());

        // Manager 권한 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_MANAGER")
                .build();

        // Manager 객체 생성
        User createdManager = User.builder()
                .email(manager.getEmail())
                .username(manager.getUsername())
                .password(passwordEncoder.encode(manager.getPassword()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .bNo(manager.getBNo())
                .build();

        // Manager 객체 저장
        return userRepository.save(createdManager);

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
     * 사용자 정보 수정 -닉네임 [유저, 매니저]
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-28
    **/
    @Transactional
    public User updateUsername(String email, UpdateNameDto updateNameDto) {
        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(닉네임)
        if (StringUtils.hasText(updateNameDto.getUsername())) {
            user.changeUser(updateNameDto.getUsername());
        }
        return user;
    }

    /**
     * 사용자 정보 수정 -비밀번호 [유저, 매니저]
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
    **/
    @Transactional
    public void updatePassword(String email, UpdatePwDto updatePwDto) {
        // 요청 유저 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        // 유저 정보 수정(비밀번호)
        if (StringUtils.hasText(updatePwDto.getCurrentPassword())) {
            if (!passwordEncoder.matches(updatePwDto.getCurrentPassword(), user.getPassword())) {
                throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
            }
        }
        user.encodingPassword(passwordEncoder.encode(updatePwDto.getNewPassword()));
    }

    /**
     * 중복 회원 검증 - 이메일
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    public void validateDuplicateEmail(String email) {
        int findUsers = userRepository.countByEmail(email);

        if (findUsers > 0) {
            throw new DuplicateUsernameException("이메일이 중복되었습니다");
        }
    }

    /**
     * 중복 회원 검증 - 닉네임
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    public void validateDuplicateUser(String username) {
        int findUsers = userRepository.countByUsername(username);

        if (findUsers > 0) {
            throw new DuplicateUsernameException("닉네임이 중복되었습니다");
        }
    }

    /**
     * 중복 가게명 검증
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-29
     **/
    //TODO

}
