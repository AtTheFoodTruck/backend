package com.sesac.domain.jwt;

import com.sesac.domain.user.entity.User;
import com.sesac.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    /**
     * 인증처리를 위한 유저 객체 조회
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        // DB에서 유저정보와 권한정보를 조회
        return userRepository.findByEmail(email)
                .map(user -> createMember(email, user))
                .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createMember(String email, User user) {
        if (!user.isActivated()) {
            throw new RuntimeException(email + " -> 활성화되어 있지 않습니다.");
        }

        //유저가 활성화 상태라면
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        // 유저의 권한, 이름, 패스워드를 갖고 userdetails.User객체를 리턴해준다
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                grantedAuthorities);
    }
}
