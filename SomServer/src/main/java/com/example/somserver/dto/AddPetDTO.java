package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class AddPetDTO {

    private String petName;
    private String breed;
    private Byte age;
    private BigDecimal currentWeight;
    private Boolean isNeutered;
    private Character gender;
    private Boolean hasDiabetes;

    private LocalTime insulinTime1;
    private LocalTime insulinTime2;
    private LocalTime insulinTime3;
    private LocalDate heartwormShotDate;
    private LocalDate heartwormMedicineDate;

    // 기본 생성자
    public AddPetDTO() {

    }
}
