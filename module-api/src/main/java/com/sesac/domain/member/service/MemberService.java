package com.sesac.domain.member.service;

import com.sesac.domain.member.dto.RequestMember;
import com.sesac.domain.member.entity.Member;
import com.sesac.domain.member.entity.Role;
import com.sesac.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(RequestMember member) {
        if (memberRepository.findByUsername(member.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        
        //TODO 유저권한을 제외하고 저장
        Member createdMember = Member.builder()
                .email(member.getEmail())
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .phoneNum(member.getPhoneNum())
                .active(true)
                .role(Role.ROLE_USER)
                .build();

        return memberRepository.save(createdMember);
    }
}
