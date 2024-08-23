package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class UpdateNextVisitDateDTO {

    private LocalDate nextVisitDate;

    // 기본 생성자
    public UpdateNextVisitDateDTO() {

    }
}
