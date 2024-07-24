package com.example.somserver.repository;

import com.example.somserver.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<PetEntity, String> {


}
