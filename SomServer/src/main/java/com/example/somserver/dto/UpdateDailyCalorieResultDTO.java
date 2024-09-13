package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class UpdateDailyCalorieResultDTO {

    private String obesityDegree;
    private BigDecimal recommendedCalories;
    private BigDecimal weightCalRecommendedCalories;
    private LocalDate calRecommendedCaloriesDate;

    // 기본 생성자
    public UpdateDailyCalorieResultDTO() {

    }
}
