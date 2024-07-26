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
@Table(name = "special_note_records")
public class SpecialNoteRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "special_note_record_id", nullable = false)
    private int specialNoteRecordId; //special_note_records table -> special_note_record_id : pk, not null, auto increment, INT

    //@Column(name = "pet_id", nullable = false, length = 20)
    //private String petId; //special_note_records table -> pet_id : not null, varchar(20)

    @Column(name = "special_note_record_date", nullable = false)
    private LocalDate specialNoteRecordDate; //special_note_records table -> special_note_record_date : not null, DATE

    @Column(name = "special_note", nullable = false, length = 200)
    private String specialNote; //special_note_records table -> special_note : not null, VARCHAR(200)

    //PetEntity(테이블 pets)와 SpecialNoteRecordEntity(테이블 special_note_records)를 0ne to many 관계로 매핑
    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false) //pet_id가 외래 키(FK)로 설정되어 pets 테이블의 pet_id 칼럼을 참조함
    private PetEntity pet;
}
