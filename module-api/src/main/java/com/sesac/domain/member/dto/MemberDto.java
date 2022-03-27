package com.sesac.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String password;
    private String phoneNum;
    private boolean active;
}
