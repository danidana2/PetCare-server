package com.example.somserver.repository;

import com.example.somserver.entity.BloodSugarLevelRecordEntity;
import com.example.somserver.entity.WeightRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecordEntity, Integer> {

    //pet_id와 weight_record_date 가 일치하는 레코드를 조회하는 메소드 작성
    @Query("SELECT wr FROM WeightRecordEntity wr WHERE wr.pet.petId = :petId AND wr.weightRecordDate = :date")
    Optional<WeightRecordEntity> findByPetIdAndDate(@Param("petId") String petId, @Param("date") LocalDate date);

    //해당 pet_id의 가장 최신의 레코드를 가져오는 메서드
    WeightRecordEntity findFirstByPetPetIdOrderByWeightRecordDateDesc(String petId);
}
