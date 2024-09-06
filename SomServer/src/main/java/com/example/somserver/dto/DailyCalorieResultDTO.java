package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class DailyCalorieResultDTO {

    private String petName;
    private String obesityDegree;
    private BigDecimal recommendedCalories;

    // 기본 생성자
    public DailyCalorieResultDTO() {

    }
}
