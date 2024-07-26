package com.example.somserver.repository;

import com.example.somserver.entity.PrescriptionRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRecordRepository extends JpaRepository<PrescriptionRecordEntity, Integer> {


}
