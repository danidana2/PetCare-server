package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddWalkingPostDTO {

    private Boolean isNicknamePublic;
    private String content;

    // 기본 생성자
    public AddWalkingPostDTO() {

    }
}
