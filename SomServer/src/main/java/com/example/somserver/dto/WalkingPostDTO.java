package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class WalkingPostDTO {

    private String walkingPostId;
    private String nickname;
    private LocalDate walkingPostDate;
    private LocalTime walkingPostTime;
    private String content;
    private String imageUrl;

    // 기본 생성자
    public WalkingPostDTO() {

    }
}
