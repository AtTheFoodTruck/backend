package com.sesac.domain.user.api;

import com.sesac.domain.user.api.dto.ApiReqStatusDto;
import com.sesac.domain.user.api.dto.ApiReqValidateDto;
import com.sesac.domain.user.dto.RequestStatusDto;
import com.sesac.domain.user.dto.RequestValidateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
//@RequiredArgsConstructor
@Configuration
public class BusinessApiRestTemplate {

    private static final String serviceKey = "JyZTPPmD5XHt0PIhYecvp1xIsQj%2B1kU%2Btw4P%2Be2UHoqKCIdQ2gM5aQvJCGDrWh4LRE9fv7YOZIlNuj2o0asNDA%3D%3D";

    //    private final RestTemplate restTemplate;
    private final RestTemplate restTemplate;

    public BusinessApiRestTemplate() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * 사업자등록번호 상태조회 API
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    public boolean statusApi(RequestStatusDto statusDto) {
        // UriComponents
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/status?serviceKey="+serviceKey)
                .build();

        // HttpEntity(body, header)
        ApiReqStatusDto apiReqStatusDto = ApiReqStatusDto.builder()
                                                        .bNo(statusDto.getBNo())
                                                        .build();
        HttpEntity<ApiReqStatusDto> entity = new HttpEntity<>(apiReqStatusDto, new HttpHeaders());

        // Request API
        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, entity, String.class);

        // Json Parsing
        JSONParser jsonParser = new JSONParser();
        JSONObject body = null;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 상태조회 실패");
            return false;
        }

        // status 가 OK 거나, 등록된 사용자 ("match_cnt" = 1)
        return "OK".equals(body.get("status_code")) && body.containsKey("match_cnt");
    }




    /**
     * 사업자등록번호 진위확인 API
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    public boolean validateApi(RequestValidateDto validateDto) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromHttpUrl("https://api.odcloud.kr/api/nts-businessman/v1/validate?serviceKey="+serviceKey)
                .build();

        // HttpEntity(body, header)
        ApiReqValidateDto apiReqValidateDto = ApiReqValidateDto.builder()
                                                            .validateDto(validateDto).build();
        HttpEntity<ApiReqValidateDto> entity = new HttpEntity<>(apiReqValidateDto, new HttpHeaders());

        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.POST, entity, String.class);

        // Json Parsing
        JSONParser jsonParser = new JSONParser();
        JSONObject body = null;
        try {
            body = (JSONObject) jsonParser.parse(response.getBody());
        } catch (ParseException e) {
            log.warn("Json Parsing Error: 사업자등록번호 진위확인 실패");
            return false;
        }

        // status 가 OK 거나, valid 가 01 인 사업자등록번호 ("valid_cnt" = 1)
        return "OK".equals(body.get("status_code")) && body.containsKey("valid_cnt");
    }
}
