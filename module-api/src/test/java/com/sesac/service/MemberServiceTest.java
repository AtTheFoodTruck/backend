package com.sesac.service;

import com.sesac.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {

    @Autowired private MemberService memberService;

    @Test
    public void save() {
        Member member = new Member();
        member.setName("jaemin");
        member.setEmail("jaemin@naver.com");

        Long returnId = memberService.signup(member);

        Assertions.assertThat(returnId).isEqualTo(1L);
    }
}
