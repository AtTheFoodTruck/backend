package com.sesac.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class BNoApiResponseDto {
    /**
     * 사업자등록번호 상태조회, 진위여부 응답
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/30
     **/
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BNoDto {
        private boolean valid;
    }
}
