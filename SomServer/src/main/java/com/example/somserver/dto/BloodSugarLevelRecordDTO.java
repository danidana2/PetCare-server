package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BloodSugarLevelRecordDTO {

    private LocalDate sugarRecordDate;
    private Short bloodSugarLevel;

    // 기본 생성자
    public BloodSugarLevelRecordDTO() {

    }

    public BloodSugarLevelRecordDTO(LocalDate sugarRecordDate, Short bloodSugarLevel) {
        this.sugarRecordDate = sugarRecordDate;
        this.bloodSugarLevel = bloodSugarLevel;
    }
}
