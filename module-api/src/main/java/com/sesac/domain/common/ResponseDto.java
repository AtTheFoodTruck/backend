package com.sesac.domain.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseDto<T> {
    int status;
    T data;

    public ResponseDto(int status) {
        this.status = status;
    }

    public ResponseDto(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
