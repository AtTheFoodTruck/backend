package com.sesac.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateTokenDto {

    private String accessToken;
    private String refreshToken;

}
