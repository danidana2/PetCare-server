package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class DiabetesRiskResultDTO {

    private String petName;
    private String diabetesRisk;
    private String recommendedNote;

    // 기본 생성자
    public DiabetesRiskResultDTO() {

    }
}
