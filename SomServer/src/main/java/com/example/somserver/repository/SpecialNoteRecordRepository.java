package com.example.somserver.repository;

import com.example.somserver.entity.BloodSugarLevelRecordEntity;
import com.example.somserver.entity.SpecialNoteRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface SpecialNoteRecordRepository extends JpaRepository<SpecialNoteRecordEntity, Integer> {

    //pet_id와 special_note_record_date 가 일치하는 레코드를 조회하는 메소드 작성
    @Query("SELECT sr FROM SpecialNoteRecordEntity sr WHERE sr.pet.petId = :petId AND sr.specialNoteRecordDate = :date")
    Optional<SpecialNoteRecordEntity> findByPetIdAndDate(@Param("petId") String petId, @Param("date") LocalDate date);
}
