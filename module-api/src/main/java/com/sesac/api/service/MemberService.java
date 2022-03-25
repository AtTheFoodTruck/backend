package com.sesac.api.service;

import com.sesac.common.entity.Member;
import com.sesac.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

//    public Long signup(Member member) {
//        return memberRepository.
//    }
}
