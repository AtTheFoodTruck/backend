//package com.sesac.domain.member.controller;
//
//import com.sesac.domain.common.ResponseDto;
//import com.sesac.domain.member.dto.RequestMember;
//import com.sesac.domain.member.dto.ResponseMember;
//import com.sesac.domain.member.entity.Member;
//import com.sesac.domain.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
//@RequiredArgsConstructor
//@RestController
//public class MemberController {
//
//    private final MemberService memberService;
//
//    /**
//     * 개인 회원가입
//     * @author jaemin
//     * @version 1.0.0
//     * 작성일 2022-03-26
//    **/
//    @PostMapping("/users/join")
//    public ResponseDto join(@Valid @RequestBody RequestMember member, BindingResult result) {
//        // validation 검증
//        if (result.hasErrors()) {
//            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getFieldError());
//        }
//
//        Member joinMember = memberService.join(member);
//        ResponseMember responseMember = new ResponseMember(joinMember);
//
//        return new ResponseDto(HttpStatus.CREATED.value(), responseMember);
//    }
//}
