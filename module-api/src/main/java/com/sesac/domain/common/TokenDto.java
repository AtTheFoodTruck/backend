package com.sesac.domain.common;

import lombok.*;

@AllArgsConstructor
@Getter
public class TokenDto {

    private String accessToken;
    private String refreshToken;
}