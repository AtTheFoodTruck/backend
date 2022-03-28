package com.sesac.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@Data
public class LoginUserDto {

    @NotBlank(message = "이메일 정보는 필수입니다.")
    @Email
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
