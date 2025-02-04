package com.example.somserver.repository;

import com.example.somserver.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PetRepository extends JpaRepository<PetEntity, String> {

    //특정 petId가 데이터베이스에 존재하는지 여부를 확인하는 메소드 작성
    Boolean existsByPetId(String petId);

    //petId을 받아 DB 테이블에서 PetEntity을 조회하는 메소드 작성
    PetEntity findByPetId(String petId);

    //userId로 모든 petId를 조회하는 메소드 작성 - 없으면 [] 빈리스트 반환
    @Query("SELECT p.petId FROM PetEntity p WHERE p.user.userId = :userId")
    List<String> findPetIdsByUserId(String userId);

    //petId를 받아 해당 레코드에서 current_weight을 조회하는 메소드 작성
    @Query("SELECT p.currentWeight FROM PetEntity p WHERE p.petId = :petId")
    BigDecimal findCurrentWeightByPetId(@Param("petId") String petId);
}
