package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DiabetesRiskDTO {

    private LocalDate diabetesRiskCheckDate;
    private String diabetesRisk;

    // 기본 생성자
    public DiabetesRiskDTO() {

    }
}
