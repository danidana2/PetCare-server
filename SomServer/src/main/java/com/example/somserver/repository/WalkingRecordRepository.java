package com.example.somserver.repository;

import com.example.somserver.entity.WalkingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingRecordRepository extends JpaRepository<WalkingRecordEntity, Integer> {


}
