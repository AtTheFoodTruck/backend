//package com.sesac.domain.member.dto;
//
//import com.sesac.domain.member.entity.Member;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;
//
//@Builder
//@AllArgsConstructor
//@Data
//public class RequestMember {
//
//    @NotNull(message = "이메일 정보는 필수입니다.")
//    @Email
//    private String email;
//
//    @NotNull(message = "닉네임 정보는 필수입니다.")
//    private String username; //닉네임
//
//    @NotNull(message = "비밀번호는 필수입니다.")
//    private String password;
//
//    @NotNull(message = "핸드폰 정보는 필수입니다.")
//    private String phoneNum;
//}
