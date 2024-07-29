package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePasswordDTO {

    private String password;

    // 기본 생성자
    public UpdatePasswordDTO() {

    }
}
