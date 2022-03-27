package com.sesac.jwt;

import com.sesac.domain.member.entity.Member;
import com.sesac.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    // TODO 유효성 검증 로직 수정    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("username 값을 찾을 수 없습니다. username= " + username)
        );

        List<GrantedAuthority> authorities = new ArrayList<>();
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
