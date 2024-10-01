package com.example.somserver.repository;

import com.example.somserver.entity.WalkingPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingPostRepository extends JpaRepository<WalkingPostEntity, String> { //id 값의 레퍼런스 타입


}
