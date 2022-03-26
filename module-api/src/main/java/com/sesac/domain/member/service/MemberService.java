package com.sesac.domain.member.service;

import com.sesac.domain.member.entity.Member;
import com.sesac.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Long signup(Member member) {
        return memberRepository.save(member).getId();
    }
}
