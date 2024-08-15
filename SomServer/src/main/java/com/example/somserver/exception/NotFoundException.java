package com.example.somserver.exception;

public class NotFoundException extends RuntimeException{

    // 기본 생성자
    public NotFoundException() {
        super("Resource not found");
    }

    // 메시지를 받는 생성자
    public NotFoundException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받는 생성자
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)만 받는 생성자
    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
