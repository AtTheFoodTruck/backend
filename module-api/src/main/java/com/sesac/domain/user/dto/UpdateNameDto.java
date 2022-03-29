package com.sesac.domain.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateNameDto {

    @NotBlank(message = "닉네임을 입력해주세요")
    private String username;

}
