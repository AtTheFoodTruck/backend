package com.sesac.domain.member.repository;

import com.sesac.domain.member.entity.Member;
import com.sesac.domain.member.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired private MemberRepository memberRepository;

    @Test
    public void add() {
        Member member = new Member();
        member.setName("jaemin");
        member.setEmail("jaemin@test.com");

        Member savedMember = memberRepository.save(member);

        assertEquals(member, memberRepository.findById(savedMember.getId()).get());
    }
}