package com.sesac.domain.user.dto;

import com.sesac.domain.user.entity.User;
import lombok.Data;

@Data
public class ResponseUser {

    private String username;
    private String email;
    private String phoneNum;

    // Entity -> Dto
    public ResponseUser(User joinMember) {
        this.username = joinMember.getUsername();
        this.email = joinMember.getEmail();
        this.phoneNum = joinMember.getPhoneNum();
    }
}
