package com.sesac.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Data
public class LoginUser {

    @NotNull(message = "이메일 정보는 필수입니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;
}
