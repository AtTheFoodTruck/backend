package com.sesac.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 사업자등록번호 진위 확인 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class RequestValidateDto {
    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    private String bNo;                         // 사업자 등록 번호
    @NotBlank(message = "개업일은 필수입니다.")
    private String startDt;                     // 개업일
    @NotBlank(message = "대표자사명은 필수입니다.")
    private String pNm;                         // 대표자사명
}
