package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PetProfileDTO {

    private String petName;
    private String breed;
    private Character gender;
    private Byte age;
    private BigDecimal currentWeight;
    private Boolean isNeutered;
    private Boolean hasDiabetes;

    // 기본 생성자
    public PetProfileDTO() {

    }
}
