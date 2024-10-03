package com.example.somserver.repository;

import com.example.somserver.entity.WalkingPostCommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingPostCommentRepository extends JpaRepository<WalkingPostCommentEntity, String> { //id 값의 레퍼런스 타입

    //walkingPostCommentId 받아 DB 테이블에서 WalkingPostCommentEntity를 조회하는 메소드 작성
    WalkingPostCommentEntity findByWalkingPostCommentId(String walkingPostCommentId);

    //walkingPostId로 특정 게시물의 해당 댓글들을 조회할 수 있도록 메소드 작성
    Page<WalkingPostCommentEntity> findByWalkingPost_WalkingPostId(String walkingPostId, Pageable pageable);
}
