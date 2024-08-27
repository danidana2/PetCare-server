package com.example.somserver.repository;

import com.example.somserver.dto.BloodSugarLevelRecordDTO;
import com.example.somserver.dto.WeightRecordDTO;
import com.example.somserver.entity.BloodSugarLevelRecordEntity;
import com.example.somserver.entity.WeightRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeightRecordRepository extends JpaRepository<WeightRecordEntity, Integer> {

    //pet_id와 weight_record_date 가 일치하는 레코드를 조회하는 메소드 작성
    @Query("SELECT wr FROM WeightRecordEntity wr WHERE wr.pet.petId = :petId AND wr.weightRecordDate = :date")
    Optional<WeightRecordEntity> findByPetIdAndDate(@Param("petId") String petId, @Param("date") LocalDate date);

    //해당 pet_id의 가장 최신의 레코드를 가져오는 메서드
    WeightRecordEntity findFirstByPetPetIdOrderByWeightRecordDateDesc(String petId);

    //petId로 검색해 weightRecordDate를 기준으로 최신 레코드에서 weight_record_date, weight 를 가져오는 메서드
    @Query("SELECT new com.example.somserver.dto.WeightRecordDTO(wr.weightRecordDate, wr.weight) " +
            "FROM WeightRecordEntity wr " +
            "WHERE wr.pet.petId = :petId " +
            "ORDER BY wr.weightRecordDate DESC")
    List<WeightRecordDTO> findByPetIdOrderByWeightRecordDateDesc(@Param("petId") String petId);
}
