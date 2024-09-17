package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DailyWalkingRecordResultDTO {

    private LocalDate recordDate;
    private Short targetWalkingTime;
    private Character walkingIntensity;
    private Short walkingTime;
    private Boolean targetWalkingResult;

    // 기본 생성자
    public DailyWalkingRecordResultDTO() {

    }
}
