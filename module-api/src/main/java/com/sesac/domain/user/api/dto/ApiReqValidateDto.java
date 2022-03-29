package com.sesac.domain.user.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sesac.domain.user.dto.RequestValidateDto;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * 사업자등록번호 상태 조회 Request
 * @author jjaen
 * @version 1.0.0
 * 작성일 2022/03/29
**/
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApiReqValidateDto {

    private List<RequestValidateDto> businesses = new ArrayList<>();  // 사업자 등록 번호

    @Builder
    public ApiReqValidateDto(RequestValidateDto validateDto) {
        this.businesses.add(validateDto);
    }
}
