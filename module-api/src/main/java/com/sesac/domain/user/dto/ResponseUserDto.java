package com.sesac.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@AllArgsConstructor
@Data
public class ResponseUserDto {

    private String username;
    private String email;
    private String phoneNum;
    // manager
    private String bNo;

    // Entity -> Dto
    public ResponseUserDto(User joinMember) {
        this.username = joinMember.getUsername();
        this.email = joinMember.getEmail();
        this.phoneNum = joinMember.getPhoneNum();
    }
}
