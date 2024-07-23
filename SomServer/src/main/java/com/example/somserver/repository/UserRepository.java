package com.example.somserver.repository;

import com.example.somserver.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//데이터베이스에 접근할 수 있는 리포지터리
public interface UserRepository extends JpaRepository<UserEntity, String> { //id 값의 레퍼런스 타입

    Boolean existsByUserId(String userId); //existsBy 구문
}
