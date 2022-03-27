package com.sesac.domain.member.repository;

import com.sesac.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Query("select m from Member m where m.email=:email")
    Optional<Member> findByEmail(String email);

}
