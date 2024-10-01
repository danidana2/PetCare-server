package com.example.somserver.repository;

import com.example.somserver.entity.WalkingPostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingPostCommentRepository extends JpaRepository<WalkingPostCommentEntity, Integer> { //id 값의 레퍼런스 타입


}
