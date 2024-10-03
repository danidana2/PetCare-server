package com.example.somserver.exception;

public class ImageDeleteErrorException extends RuntimeException{

    // 기본 생성자
    public ImageDeleteErrorException() {
        super("ImageDeleteError occurred");
    }

    // 메시지를 받을 수 있는 생성자
    public ImageDeleteErrorException(String message) {
        super(message);
    }

    // 메시지와 원인(cause)을 받을 수 있는 생성자
    public ImageDeleteErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인(cause)을 받을 수 있는 생성자
    public ImageDeleteErrorException(Throwable cause) {
        super(cause);
    }
}
