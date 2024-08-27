package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class WeightRecordDTO {

    private LocalDate weightRecordDate;
    private BigDecimal weight;

    // 기본 생성자
    public WeightRecordDTO() {

    }

    public WeightRecordDTO(LocalDate weightRecordDate, BigDecimal weight) {
        this.weightRecordDate = weightRecordDate;
        this.weight = weight;
    }
}
