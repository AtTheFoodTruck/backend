package com.sesac.domain.user.repository;

import com.sesac.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // 중복 닉네임 검증
    int countByUsername(String username);

    // 중복 이메일 검증
    int countByEmail(String email);
}
