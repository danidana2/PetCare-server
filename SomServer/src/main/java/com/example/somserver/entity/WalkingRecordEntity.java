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
@Table(name = "walking_records")
public class WalkingRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walking_record_id", nullable = false)
    private int walkingRecordId; //walking_records table -> walking_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //walking_records table -> pet_id : not null, varchar(20)

    @Column(name = "walking_record_date", nullable = false)
    private LocalDate walkingRecordDate; //walking_records table -> walking_record_date : not null, DATE

    @Column(name = "target_walking_time", nullable = false)
    private Short targetWalkingTime; //walking_records table -> target_walking_time : not null, SMALLINT

    @Column(name = "walking_intensity", nullable = false)
    private Character walkingIntensity; //walking_records table -> walking_intensity : not null, CHAR(1)

    @Column(name = "walking_time", nullable = false)
    private Short walkingTime; //walking_records table -> walking_time: not null, SMALLINT

    @Column(name = "target_walking_result", nullable = false)
    private Boolean targetWalkingResult; //walking_records table -> target_walking_result: not null, TINYINT(1)

    //PetEntity(테이블 pets)와 WalkingRecordEntity(테이블 walking_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
