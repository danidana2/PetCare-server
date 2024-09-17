package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class AlarmSettingDTO {

    private Boolean insulinTimeAlarm;
    private Boolean nextVisitAlarm;
    private Boolean heartwormAlarm;
    private Boolean walkingAlarm;

    // 기본 생성자
    public AlarmSettingDTO() {

    }
}
