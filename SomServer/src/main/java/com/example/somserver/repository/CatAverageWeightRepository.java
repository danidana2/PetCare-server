package com.example.somserver.repository;

import com.example.somserver.entity.CatAverageWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CatAverageWeightRepository extends JpaRepository<CatAverageWeightEntity, Integer> {

    //cat_breed를 받아 DB 테이블에서 CatAverageWeightEntity를 조회하는 메소드 작성
    CatAverageWeightEntity findByCatBreed(String catBreed);
}
