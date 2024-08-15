package com.example.somserver.exception;

public class ConflictException extends RuntimeException{

    // 기본 생성자
    public ConflictException() {
        super("Conflict occurred");
    }

    // 메시지를 받을 수 있는 생성자
    public ConflictException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받을 수 있는 생성자
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)을 받을 수 있는 생성자
    public ConflictException(Throwable cause) {
        super(cause);
    }
}
