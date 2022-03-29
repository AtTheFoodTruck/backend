package com.sesac.domain.user.controller;

import com.sesac.domain.common.ResponseDto;
import com.sesac.domain.user.api.BusinessApiRestTemplate;
import com.sesac.domain.user.dto.RequestStatusDto;
import com.sesac.domain.user.dto.RequestValidateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/managers")
@RestController
public class ApiController {

    private final BusinessApiRestTemplate apiRestTemplate;

    @GetMapping("/health_check")
    public String health() {
        return "health_check";
    }

    /**
     * 사업자등록번호 상태조회
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    @PostMapping("/status")
    public ResponseDto bNoStatus(@RequestBody RequestStatusDto statusDto) {
//    public ResponseDto bNoStatus(@PathVariable("id") String id) {
//            RequestStatusDto statusDto = new RequestStatusDto(id);

//        if (result.hasErrors()) {
//            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getFieldError());
//        }

        if (!apiRestTemplate.statusApi(statusDto)){
            return new ResponseDto(HttpStatus.OK.value(), "사업자 조회 실패");
        }

        return new ResponseDto(HttpStatus.OK.value(), "사업자 조회 성공");
    }

    /**
     * 사업자등록번호 진위여부
     * @author jjaen
     * @version 1.0.0
     * 작성일 2022/03/29
    **/
    @PostMapping("/validate")
    public ResponseDto bNoValidate(@Valid @RequestBody RequestValidateDto validateDto, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getFieldError());
        }

        if (!apiRestTemplate.validateApi(validateDto))
            return new ResponseDto(HttpStatus.BAD_REQUEST.value());

        return new ResponseDto(HttpStatus.OK.value());
    }
}
