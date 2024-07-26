package com.example.somserver.repository;

import com.example.somserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//데이터베이스에 접근할 수 있는 리포지터리
public interface UserRepository extends JpaRepository<UserEntity, String> { //id 값의 레퍼런스 타입

    //특정 userId가 데이터베이스에 존재하는지 여부를 확인하는 메소드 작성
    Boolean existsByUserId(String userId);

    //userId을 받아 DB 테이블에서 회원을 조회하는 메소드 작성
    UserEntity findByUserId(String userId);
}
