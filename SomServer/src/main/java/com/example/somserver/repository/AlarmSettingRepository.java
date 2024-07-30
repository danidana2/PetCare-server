package com.example.somserver.repository;

import com.example.somserver.entity.AlarmSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmSettingRepository extends JpaRepository<AlarmSettingEntity, Integer> {

}
