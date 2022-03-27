package com.sesac.domain.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.domain.member.dto.MemberDto;
import com.sesac.domain.member.dto.RequestMember;
import com.sesac.domain.member.service.MemberService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final MemberService memberService;
    private final Environment env;

    /**
     * 요청정보를 처리 담당
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            RequestMember creds = new ObjectMapper().readValue(request.getInputStream(), RequestMember.class);

            // token을 인증 처리
            return getAuthenticationManager().authenticate(
                    // usernamepasswordAuthentication Token으로 변경해야지 usernamepasswordAuthenticationFilter로 전송가능
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 인증처리 성공 후처리
     * @author jaemin
     * @version 1.0.0
     * 작성일 2022-03-27
    **/
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        // username 추출
        String userName= ((User) authResult.getPrincipal()).getUsername();
        //token 생성하기 이전에 userId로 토큰을 생성할 예정, DB에서 userDto를 갖고와서 token 생성

        MemberDto memberDetails = memberService.getMemberDetailsByEmail(userName);
        String token = Jwts.builder()
                .setSubject(String.valueOf(memberDetails.getId()))
                .setExpiration(new Date(System.currentTimeMillis() +
                        Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();
        response.addHeader("Authorization", token);
        response.addHeader("memberId", String.valueOf(memberDetails.getId()));
    }
}
