package com.sesac.domain.member.entity;

import com.sesac.domain.member.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
@SpringBootTest
public class MemberTest {

    @Autowired
    private MemberRepository memberRepository;
    @PersistenceContext
    private EntityManager em;

    @Test
    public void BaseEntity_테스트() throws InterruptedException {
        //given
//        Member member = new Member();
//        member.setName("jaemin");
//        member.setEmail("jaemin@naver.com");
//        memberRepository.save(member);
//
//        Thread.sleep(100);
//        member.setName("jaeminUpdate");
//        member.setEmail("jaeminUpdate@naver.com"); //modify
//
//        em.flush();
//        em.clear();
//
//        //when
//        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
//                () -> new IllegalArgumentException("id에 해당하는 멤버 없음. id = " + member.getId())
//        );
//
//        //then
//        System.out.println("createdDate = " + findMember.getCreatedDate());
//        System.out.println("createdBy = " + findMember.getCreatedBy());
//        System.out.println("modifiedDate = " + findMember.getModifiedDate());
//        System.out.println("modifiedBy = " + findMember.getModifiedBy());
    }
}