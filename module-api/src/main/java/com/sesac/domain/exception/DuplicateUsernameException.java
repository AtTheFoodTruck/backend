package com.sesac.domain.exception;

public class DuplicateUsernameException extends RuntimeException{
    public DuplicateUsernameException(String message) {
        super(message);
    }
}
