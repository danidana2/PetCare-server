package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class DailyRecordDTO {

    private LocalDate recordDate;

    private String diagnosis;
    private String medicine;

    private BigDecimal weight;

    private Short bloodSugarLevel;

    private String specialNote;

    // 기본 생성자
    public DailyRecordDTO() {

    }
}
