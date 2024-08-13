package com.example.somserver.repository;

import com.example.somserver.entity.PrescriptionRecordEntity;
import com.example.somserver.entity.WeightRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PrescriptionRecordRepository extends JpaRepository<PrescriptionRecordEntity, Integer> {

    //pet_id와 prescription_record_date 가 일치하는 레코드를 조회하는 메소드 작성
    @Query("SELECT pr FROM PrescriptionRecordEntity pr WHERE pr.pet.petId = :petId AND pr.prescriptionRecordDate = :date")
    Optional<PrescriptionRecordEntity> findByPetIdAndDate(@Param("petId") String petId, @Param("date") LocalDate date);
}
