package com.example.somserver.repository;

import com.example.somserver.entity.DogAverageWeightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogAverageWeightRepository extends JpaRepository<DogAverageWeightEntity, Integer> {

    //dog_breed, dog_is_neutered, dog_gender를 받아 DB 테이블에서 DogAverageWeightEntity 조회하는 메소드 작성
    DogAverageWeightEntity findByDogBreedAndDogIsNeuteredAndDogGender(String dogBreed, Boolean dogIsNeutered, Character dogGender);
}
