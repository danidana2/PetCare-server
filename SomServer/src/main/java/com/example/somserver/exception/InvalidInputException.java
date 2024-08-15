package com.example.somserver.exception;

public class InvalidInputException extends RuntimeException{

    // 기본 생성자
    public InvalidInputException() {
        super("Invalid input provided");
    }

    // 메시지를 받는 생성자
    public InvalidInputException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받는 생성자
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)만 받는 생성자
    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
