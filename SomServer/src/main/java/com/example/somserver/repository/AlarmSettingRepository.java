package com.example.somserver.repository;

import com.example.somserver.entity.AlarmSettingEntity;
import com.example.somserver.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmSettingRepository extends JpaRepository<AlarmSettingEntity, Integer> {

    //userId를 받아 DB 테이블에서 AlarmSettingEntity를 조회하는 메소드 작성
    AlarmSettingEntity findByUserUserId(String userId);
}
