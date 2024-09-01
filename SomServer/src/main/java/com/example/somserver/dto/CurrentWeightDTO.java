package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CurrentWeightDTO {

    private BigDecimal currentWeight;

    // 기본 생성자
    public CurrentWeightDTO() {

    }
}
