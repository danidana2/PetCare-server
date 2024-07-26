package com.example.somserver.repository;

import com.example.somserver.entity.BloodSugarLevelRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodSugarLevelRecordRepository extends JpaRepository<BloodSugarLevelRecordEntity, Integer> {


}
