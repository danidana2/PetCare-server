package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateNicknameDTO {

    private String nickname;

    // 기본 생성자
    public UpdateNicknameDTO() {

    }
}
