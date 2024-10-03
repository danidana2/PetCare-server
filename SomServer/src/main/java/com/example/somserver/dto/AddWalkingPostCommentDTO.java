package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddWalkingPostCommentDTO {

    private Boolean isNicknamePublic;
    private String comment;

    // 기본 생성자
    public AddWalkingPostCommentDTO() {

    }
}
