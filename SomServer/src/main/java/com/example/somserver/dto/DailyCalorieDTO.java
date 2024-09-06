package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class DailyCalorieDTO {

    private LocalDate calRecommendedCaloriesDate;
    private BigDecimal recommendedCalories;

    // 기본 생성자
    public DailyCalorieDTO() {

    }
}
