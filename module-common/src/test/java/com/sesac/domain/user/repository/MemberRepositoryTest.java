package com.sesac.domain.user.repository;

import com.sesac.domain.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    public void add() {
        User user = User.builder()
                .username("jaemin")
                .email("jaemin@test")
                .build();

        User savedUser = userRepository.save(user);

        assertEquals(user, userRepository.findById(savedUser.getId()).get());
    }
}