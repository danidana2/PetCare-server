package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pets")
public class PetEntity {

    @Id
    @Column(name = "pet_id", nullable = false, length = 20)
    private String petId; //pets table -> pet_id : pk, not null, varchar(20)
    //프론트 앱에서 user_id+pet_name 값을 pet_id 값으로 보내기

    @Column(name = "pet_name", nullable = false, length = 10)
    private String petName; //pets table -> pet_name : not null, varchar(10)

    //@Column(name = "user_id", nullable = false, length = 10)
    //private String userId; //pets table -> user_id : not null, varchar(10)

    @Column(name = "breed", nullable = false, length = 6)
    private String breed; //pets table -> breed : not null, varchar(6)

    @Column(name = "age", nullable = false)
    private byte age; //pets table -> age : not null, TINYINT

    @Column(name = "current_weight", precision =4, scale = 1, nullable = false)
    private BigDecimal currentWeight; //pets table -> current_weight : not null, DECIMAL(4,1)

    @Column(name = "is_neutered", nullable = false)
    private Boolean isNeutered; //pets table -> is_neutered : not null, TINYINT(1)

    @Column(name = "gender", nullable = false, length = 1)
    private Character gender; //pets table -> gender : not null, CHAR(1)

    @Column(name = "has_diabetes", nullable = false)
    private Boolean hasDiabetes; //pets table -> has_diabetes : not null, TINYINT(1)

    @Column(name = "insulin_time1")
    private LocalTime insulinTime1; //pets table -> insulin_time1 : TIME

    @Column(name = "insulin_time2")
    private LocalTime insulinTime2; //pets table -> insulin_time2 : TIME

    @Column(name = "insulin_time3")
    private LocalTime insulinTime3; //pets table -> insulin_time3 : TIME

    @Column(name = "heartworm_shot_date")
    private LocalDate heartwormShotDate; //pets table -> heartworm_shot_date : DATE

    @Column(name = "heartworm_medicine_date")
    private LocalDate heartwormMedicineDate; //pets table -> heartworm_medicine_date : DATE

    @Column(name = "current_blood_sugar_level")
    private Short currentBloodSugarLevel; //pets table -> current_blood_sugar_level : SMALLINT

    @Column(name = "next_visit_date")
    private LocalDate nextVisitDate; //pets table -> next_visit_date : DATE

    @Column(name = "target_momentum")
    private Short targetMomentum; //pets table -> target_momentum : SMALLINT

    @Column(name = "exercise_time")
    private LocalTime exerciseTime; //pets table -> exercise_time : TIME

    @Column(name = "obesity_degree", length = 3)
    private String obesityDegree; //pets table -> obesity_degree : VARCHAR(3)

    @Column(name = "recommended_calories", precision = 7, scale = 2)
    private BigDecimal recommendedCalories; //pets table -> recommended_calories : DECIMAL(7,2)

    @Column(name = "weight_cal_recommended_calories", precision = 4, scale = 1)
    private BigDecimal weightCalRecommendedCalories; //pets table -> weight_cal_recommended_calories : DECIMAL(4,1)

    //UserEntity(테이블 users)와 PetEntity(테이블 pets)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}
