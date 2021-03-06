package com.sesac.domain.user.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.domain.user.dto.request.BNoApiRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 사업자등록번호 상태 조회 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
@Getter
public class ApiReqValidateDto {

    private List<InnerDto> businesses = new ArrayList<>();  // 사업자 등록 번호

    @Getter
    public static class InnerDto {
        @JsonProperty("b_no")
        private String bNo;                         // 사업자 등록 번호
        @JsonProperty("start_dt")
        private String startDt;                     // 개업일
        @JsonProperty("p_nm")
        private String pNm;                         // 대표자사명

        public InnerDto(BNoApiRequestDto.BNoValidateDto bNoValidateDto) {
            this.bNo = bNoValidateDto.getBNo();
            this.startDt = bNoValidateDto.getStartDt();
            this.pNm = bNoValidateDto.getPNm();
        }
    }
    @Builder
    public ApiReqValidateDto(BNoApiRequestDto.BNoValidateDto bNoValidateDto) {
        this.businesses.add(new InnerDto(bNoValidateDto));
    }
}
