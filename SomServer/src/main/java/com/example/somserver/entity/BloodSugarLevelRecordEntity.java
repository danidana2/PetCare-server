package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blood_sugar_level_records")
public class BloodSugarLevelRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sugar_record_id", nullable = false)
    private int sugarRecordId; //blood_sugar_level_records table -> sugar_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //blood_sugar_level_records table -> pet_id : not null, varchar(20)

    @Column(name = "sugar_record_date", nullable = false)
    private LocalDate sugarRecordDate; //blood_sugar_level_records table -> sugar_record_date : not null, DATE

    @Column(name = "blood_sugar_level", nullable = false)
    private Short bloodSugarLevel; //blood_sugar_level_records table -> blood_sugar_level : not null, SMALLINT

    //PetEntity(테이블 pets)와 BloodSugarLevelRecordEntity(테이블 blood_sugar_level_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
