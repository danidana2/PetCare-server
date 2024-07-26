package com.example.somserver.repository;

import com.example.somserver.entity.WeightRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRecordRepository extends JpaRepository<WeightRecordEntity, Integer> {


}
