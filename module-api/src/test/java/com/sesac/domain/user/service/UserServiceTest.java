package com.sesac.domain.user.service;

import com.sesac.domain.user.dto.request.JoinUserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void 회원가입() {
        //given
        JoinUserDto member = JoinUserDto.builder()
                .email("jaemin@naver.com")
                .username("jaemin")
                .password(passwordEncoder.encode("kadslfjiew"))
                .phoneNum("01077986546")
                .build();

        System.out.println("member.getClass() = " + member.getClass());
        //when
//        User savedUser = userService.signUpUser(member);
//        System.out.println("savedMember.getClass() = " + savedUser.getClass());
        
        //then
//        Assertions.assertThat(savedUser.getUsername()).isEqualTo("jaemin");
        
    }
}
