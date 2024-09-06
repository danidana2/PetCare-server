package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailyCalorieCheckDTO {

    private String weightStatus;
    private Character waistRibVisibility;
    private Character ribTouchability;
    private Integer bodyShape;
    private String activityLevel;

    // 기본 생성자
    public DailyCalorieCheckDTO() {

    }
}
