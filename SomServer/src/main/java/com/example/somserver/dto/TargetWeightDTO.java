package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TargetWeightDTO {

    private BigDecimal targetWeight;

    // 기본 생성자
    public TargetWeightDTO() {

    }
}
