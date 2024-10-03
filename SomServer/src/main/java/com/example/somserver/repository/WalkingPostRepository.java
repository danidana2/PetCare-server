package com.example.somserver.repository;

import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.WalkingPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingPostRepository extends JpaRepository<WalkingPostEntity, String> { //id 값의 레퍼런스 타입

    //walkingPostId 받아 DB 테이블에서 WalkingPostEntity를 조회하는 메소드 작성
    WalkingPostEntity findByWalkingPostId(String walkingPostId);
}
