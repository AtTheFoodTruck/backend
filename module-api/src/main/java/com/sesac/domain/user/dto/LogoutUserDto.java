package com.sesac.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class LogoutUserDto {

    @NotBlank(message = "잘못된 요청입니다.")
    private String accessToken;

    @NotBlank(message = "잘못된 요청입니다.")
    private String refreshToken;
}
