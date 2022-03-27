package com.sesac.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

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
