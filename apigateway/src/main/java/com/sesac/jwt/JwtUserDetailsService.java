package com.sesac.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username);

        List<GrantedAuthority> roles = new ArrayList<>();

        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (member.getGrade() == 0) {
            roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            roles.add(new SimpleGrantedAuthority("ROLE_USER"));
            roles.add(new SimpleGrantedAuthority("ROLE_HI"));
        }
        return new User(member.getUsername(), member.getPassword(), roles);
    }
}
