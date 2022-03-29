package com.sesac.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@Data
public class RequestUserDto {

    @Email(message = "이메일 형식을 지켜주세요")
    @NotBlank(message = "이메일 정보는 필수입니다.")
    private String email;

    @NotBlank(message = "닉네임 정보는 필수입니다.")
    private String username; //닉네임

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "핸드폰 정보는 필수입니다.")
    private String phoneNum;
}
