package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class DailyCalorieResultDTO {

    //프론트에서 보여줄 정보 + 프론트에서 다음 저장단계에서 보내줄 정보
    private String petName;
    private String obesityDegree;
    private BigDecimal recommendedCalories;

    //프론트에서 다음 저장단계에서 보내줄 정보
    private BigDecimal weightCalRecommendedCalories;
    private LocalDate calRecommendedCaloriesDate;

    // 기본 생성자
    public DailyCalorieResultDTO() {

    }
}
