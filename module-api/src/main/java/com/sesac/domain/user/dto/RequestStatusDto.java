package com.sesac.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 사업자등록번호 상태 조회 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusDto {

//    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    private String bNo;  // 사업자 등록 번호
}
