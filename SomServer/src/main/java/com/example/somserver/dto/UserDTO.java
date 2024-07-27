package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {

    private String userId;
    private String password;
    private String nickname;
    private String role;

    // 기본 생성자
    public UserDTO() {

    }
}
