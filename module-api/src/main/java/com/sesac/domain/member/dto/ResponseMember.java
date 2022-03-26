package com.sesac.domain.member.dto;

import com.sesac.domain.member.entity.Member;
import lombok.Data;

@Data
public class ResponseMember {

    private String username;
    private String email;
    private String phoneNum;

    // Entity -> Dto
    public ResponseMember(Member joinMember) {
        this.username = joinMember.getUsername();
        this.email = joinMember.getEmail();
        this.phoneNum = joinMember.getPhoneNum();
    }
}
