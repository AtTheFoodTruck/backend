package com.sesac.domain.handler;

import com.sesac.domain.common.ResponseDto;
import com.sesac.domain.user.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Response response;

    @ExceptionHandler(value=Exception.class)
    public ResponseEntity<?> handleArgumentException(Exception e) {
        return response.fail(e.getMessage(), HttpStatus.BAD_REQUEST); // 500
    }

}
