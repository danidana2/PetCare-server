package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class WalkingPostCommentDTO {

    private String walkingPostCommentId;
    private String nickname;
    private LocalDate walkingPostCommentDate;
    private LocalTime walkingPostCommentTime;
    private String comment;

    // 기본 생성자
    public WalkingPostCommentDTO() {

    }
}
