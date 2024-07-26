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
@Table(name = "achieved_target_momentum_records")
public class AchievedTargetMomentumRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achieved_target_momentum_record_id", nullable = false)
    private int achievedTargetMomentumRecordId; //achieved_target_momentum_records table -> achieved_target_momentum_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //achieved_target_momentum_records table -> pet_id : not null, varchar(20)

    @Column(name = "achieved_target_momentum_record_date", nullable = false)
    private LocalDate achievedTargetMomentumRecordDate; //achieved_target_momentum_records table -> achieved_target_momentum_record_date : not null, DATE

    @Column(name = "achieved_target_momentum", nullable = false)
    private Short achievedTargetMomentum; //achieved_target_momentum_records table -> achieved_target_momentum : not null, SMALLINT

    //PetEntity(테이블 pets)와 AchievedTargetMomentumRecordEntity(테이블 achieved_target_momentum_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
