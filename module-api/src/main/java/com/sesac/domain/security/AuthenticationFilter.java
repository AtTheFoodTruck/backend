//package com.sesac.domain.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sesac.domain.user.dto.UserDto;
//import com.sesac.domain.user.dto.RequestUser;
//import com.sesac.domain.user.service.UserService;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.env.Environment;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//@RequiredArgsConstructor
//@Slf4j
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserService userService;
//    private final Environment env;
//
//    /**
//     * 요청정보를 처리 담당
//     * @author jaemin
//     * @version 1.0.0
//     * 작성일 2022-03-27
//    **/
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request,
//                                                HttpServletResponse response) throws AuthenticationException {
//        try {
//            RequestUser creds = new ObjectMapper().readValue(request.getInputStream(), RequestUser.class);
//
//            // token을 인증 처리
//            return getAuthenticationManager().authenticate(
//                    // usernamepasswordAuthentication Token으로 변경해야지 usernamepasswordAuthenticationFilter로 전송가능
//                    new UsernamePasswordAuthenticationToken(
//                            creds.getEmail(),
//                            creds.getPassword(),
//                            new ArrayList<>())
//            );
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//}
