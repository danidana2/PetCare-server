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
@Table(name = "prescription_records")
public class PrescriptionRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_record_id", nullable = false)
    private int prescriptionRecordId; //prescription_records table -> prescription_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //prescription_records table -> pet_id : not null, varchar(20)

    @Column(name = "prescription_record_date", nullable = false)
    private LocalDate prescription_record_date; //prescription_records table -> prescription_record_date : not null, DATE

    @Column(name = "diagnosis", nullable = false, length = 20)
    private String diagnosis; //prescription_records table -> diagnosis : not null, VARCHAR(20)

    @Column(name = "medicine", length = 20)
    private String medicine; //prescription_records table -> medicine : VARCHAR(20)

    @Column(name = "once_dosage", precision = 7, scale = 4)
    private BigDecimal onceDosage; //prescription_records table -> once_dosage : DECIMAL(7,4)

    @Column(name = "direction", length = 50)
    private String direction; //prescription_records table -> direction : VARCHAR(50)

    @Column(name = "days")
    private Byte days; //prescription_records table -> days : TINYINT

    //PetEntity(테이블 pets)와 PrescriptionRecordEntity(테이블 prescription_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
