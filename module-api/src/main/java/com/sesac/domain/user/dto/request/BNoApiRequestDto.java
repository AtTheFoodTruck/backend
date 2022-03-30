package com.sesac.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class BNoApiRequestDto {

    /**
     * 사업자등록번호 상태 조회 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoStatusDto {

        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        @JsonProperty("b_no")
        private String bNo;  // 사업자 등록 번호
    }

    /**
     * 사업자등록번호 진위 확인 요청
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoValidateDto {
        @NotBlank(message = "사업자 등록번호는 필수입니다.")
        private String bNo;                         // 사업자 등록 번호
        @NotBlank(message = "개업일은 필수입니다.")
        private String startDt;                     // 개업일
        @NotBlank(message = "대표자사명은 필수입니다.")
        private String pNm;                         // 대표자사명
    }
}
