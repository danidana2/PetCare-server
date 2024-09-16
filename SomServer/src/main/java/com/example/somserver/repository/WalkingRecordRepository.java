package com.example.somserver.repository;

import com.example.somserver.entity.WalkingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface WalkingRecordRepository extends JpaRepository<WalkingRecordEntity, Integer> {

    // petId와 walkingRecordDate가 일치하는 레코드가 존재하는지 확인
    @Query("SELECT COUNT(wr) > 0 FROM WalkingRecordEntity wr WHERE wr.pet.petId = :petId AND wr.walkingRecordDate = :walkingRecordDate")
    boolean existsWalkingRecord(@Param("petId") String petId, @Param("walkingRecordDate") LocalDate walkingRecordDate);
}

