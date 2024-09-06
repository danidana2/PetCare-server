package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiabetesRiskCheckDTO {

    private Character isObesity;
    private String dailyWaterIntake;
    private String foodIntake;
    private Character isWeightLoss;
    private Character isIncreasedUrination;

    // 기본 생성자
    public DiabetesRiskCheckDTO() {

    }
}
