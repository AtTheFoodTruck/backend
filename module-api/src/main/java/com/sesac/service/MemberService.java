package com.sesac.service;

import com.sesac.entity.Member;
import com.sesac.repository.MemberRepository;
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
