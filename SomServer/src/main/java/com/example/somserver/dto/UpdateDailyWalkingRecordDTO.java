package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UpdateDailyWalkingRecordDTO {

    private LocalDate recordDate;
    private Character walkingIntensity;
    private Short walkingTime;

    // 기본 생성자
    public UpdateDailyWalkingRecordDTO() {

    }
}
