package com.example.somserver.repository;

import com.example.somserver.entity.PetFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetFoodRepository extends JpaRepository<PetFoodEntity, Integer> {


}
