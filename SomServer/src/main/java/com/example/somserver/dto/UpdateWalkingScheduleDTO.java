package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class UpdateWalkingScheduleDTO {

    private LocalTime walkingSchedule;

    // 기본 생성자
    public UpdateWalkingScheduleDTO() {

    }
}
