package com.example.somserver.service;

import com.example.somserver.dto.UpdateCurrentTargetWalkingTimeDTO;
import com.example.somserver.dto.UpdateWalkingScheduleDTO;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalTime;

@Service
public class WalkingManagementService {

    //PetRepository 주입받기
    private final PetRepository petRepository;

    public WalkingManagementService(PetRepository petRepository) {

        this.petRepository = petRepository;
    }

    //current-target-walking-time update api
    @Transactional
    public boolean updateCurrentTargetWalkingTime(String petId, UpdateCurrentTargetWalkingTimeDTO updateCurrentTargetWalkingTimeDTO) {

        //UpdateCurrentTargetWalkingTimeDTO 에서 값 꺼내야함
        Short currentTargetWalkingTime = updateCurrentTargetWalkingTimeDTO.getCurrentTargetWalkingTime();
        if (currentTargetWalkingTime == null) {
            //요청된 목표 산책 시간이 null인 경우
            throw new InvalidInputException("CurrentTargetWalkingTime is null: You must use delete api");
        }

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //pets 테이블의 current_target_walking_time 에 값 저장
        data.setCurrentTargetWalkingTime(currentTargetWalkingTime);

        petRepository.save(data);

        return true;
    }

    //current-target-walking-time delete api
    @Transactional
    public boolean deleteCurrentTargetWalkingTime(String petId) {

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setCurrentTargetWalkingTime(null);

        petRepository.save(data);

        return true;
    }

    //current-target-walking-time get api
    public Short getCurrentTargetWalkingTime(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return petEntity.getCurrentTargetWalkingTime();
    }

    //walking-schedule update api
    @Transactional
    public boolean updateWalkingSchedule(String petId, UpdateWalkingScheduleDTO updateWalkingScheduleDTO) {

        //UpdateWalkingScheduleDTO 에서 값 꺼내야함
        LocalTime walkingSchedule = updateWalkingScheduleDTO.getWalkingSchedule();
        if (walkingSchedule == null) {
            //요청된 산책 시각이 null인 경우
            throw new InvalidInputException("WalkingSchedule is null: You must use delete api");
        }

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //pets 테이블의 walking_schedule 에 값 저장
        data.setWalkingSchedule(walkingSchedule);

        petRepository.save(data);

        return true;
    }

    //walking-schedule delete api
    @Transactional
    public boolean deleteWalkingSchedule(String petId) {

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setWalkingSchedule(null);

        petRepository.save(data);

        return true;
    }

    //walking-schedule get api
    public LocalTime getWalkingSchedule(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return petEntity.getWalkingSchedule();
    }
}
