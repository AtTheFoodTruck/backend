package com.sesac.domain.user.service;

import com.sesac.domain.user.dto.RequestUser;
import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User join(RequestUser member) {
        if (userRepository.findByUsername(member.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        
        //TODO 유저권한을 제외하고 저장
        User createdMember = User.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .phoneNum(member.getPhoneNum())
                .activated(true)
                .build();

        return userRepository.save(createdMember);
    }
}
