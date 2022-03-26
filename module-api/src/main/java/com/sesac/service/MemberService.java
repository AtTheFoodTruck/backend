package com.sesac.service;

import com.sesac.entity.Member;
import com.sesac.repository.MemberRepository;
import org.springframework.stereotype.Service;

//@RequiredArgsConstructor
@Service
public class MemberService {

//    private final MemberRepository memberRepository;

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long signup(Member member) {
        return memberRepository.save(member).getId();
    }
}
