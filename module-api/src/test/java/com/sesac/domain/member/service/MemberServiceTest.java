package com.sesac.domain.member.service;

import com.sesac.domain.member.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired private MemberService memberService;

    @Test
    public void save() {
        //given
        Member member = new Member();
        member.setName("jaemin");
        member.setEmail("jaemin@naver.com");

        //when
        Long returnId = memberService.signup(member);

        //then
        Assertions.assertThat(returnId).isEqualTo(1L);
    }
}
