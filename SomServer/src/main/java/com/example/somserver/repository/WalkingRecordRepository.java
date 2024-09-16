package com.example.somserver.repository;

import com.example.somserver.entity.WalkingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface WalkingRecordRepository extends JpaRepository<WalkingRecordEntity, Integer> {

    // petId와 walkingRecordDate가 일치하는 레코드가 존재하는지 확인
    @Query("SELECT COUNT(wr) > 0 FROM WalkingRecordEntity wr WHERE wr.pet.petId = :petId AND wr.walkingRecordDate = :date")
    boolean existsWalkingRecord(@Param("petId") String petId, @Param("date") LocalDate date);

    //petId와 walkingRecordDate가 일치하는 레코드 가져오는 메서드 작성
    @Query("SELECT wr FROM WalkingRecordEntity wr WHERE wr.pet.petId = :petId AND wr.walkingRecordDate = :date")
    Optional<WalkingRecordEntity> findWalkingRecordByPetIdAndDate(@Param("petId") String petId, @Param("date") LocalDate date);
}

