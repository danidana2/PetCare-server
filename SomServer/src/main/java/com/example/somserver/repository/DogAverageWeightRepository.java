package com.example.somserver.repository;

import com.example.somserver.entity.DogAverageWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogAverageWeightRepository extends JpaRepository<DogAverageWeightEntity, Integer> {


}
