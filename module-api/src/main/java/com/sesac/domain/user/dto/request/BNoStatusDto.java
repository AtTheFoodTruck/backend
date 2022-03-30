package com.sesac.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 사업자등록번호 상태 조회 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
//@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class BNoStatusDto {

    @NotBlank(message = "사업자 등록번호는 필수입니다.")
    @JsonProperty("b_no")
    private String bNo;  // 사업자 등록 번호
}
