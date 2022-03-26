package com.sesac.domain.member.service;

import com.sesac.domain.member.dto.RequestMember;
import com.sesac.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void 회원가입() {
        //given
        RequestMember member = RequestMember.builder()
                .email("jaemin@naver.com")
                .username("jaemin")
                .password(passwordEncoder.encode("kadslfjiew"))
                .phoneNum("01077986546")
                .build();

        System.out.println("member.getClass() = " + member.getClass());
        //when
        Member savedMember = memberService.join(member);
        System.out.println("savedMember.getClass() = " + savedMember.getClass());
        
        //then
        Assertions.assertThat(savedMember.getUsername()).isEqualTo("jaemin");
        
    }
}
