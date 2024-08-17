package com.example.somserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PetProfileSummaryDTO {

    private String petName;
    private Byte age;
    private BigDecimal currentWeight;

    // 기본 생성자
    public PetProfileSummaryDTO() {

    }
}
