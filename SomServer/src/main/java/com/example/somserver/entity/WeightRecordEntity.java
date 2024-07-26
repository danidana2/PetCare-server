package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "weight_records")
public class WeightRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_record_id", nullable = false)
    private int weightRecordId; //weight_records table -> weight_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //weight_records table -> pet_id : not null, varchar(20)

    @Column(name = "weight_record_date", nullable = false)
    private LocalDate weightRecordDate; //weight_records table -> weight_record_date : not null, DATE

    @Column(name = "weight", nullable = false, precision = 4, scale = 1)
    private BigDecimal weight; //weight_records table -> weight : not null, DECIMAL(4,1)

    //PetEntity(테이블 pets)와 WeightRecordEntity(테이블 weight_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
