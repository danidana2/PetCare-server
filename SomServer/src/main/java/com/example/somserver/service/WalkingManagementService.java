package com.example.somserver.service;

import com.example.somserver.dto.*;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.WalkingRecordEntity;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.WalkingRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class WalkingManagementService {

    //PetRepository, WalkingRecordRepository 주입받기
    private final PetRepository petRepository;
    private final WalkingRecordRepository walkingRecordRepository;

    public WalkingManagementService(PetRepository petRepository,WalkingRecordRepository walkingRecordRepository) {

        this.petRepository = petRepository;
        this.walkingRecordRepository = walkingRecordRepository;
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

    //daily-walking-record add api
    @Transactional
    public boolean addDailyWalkingRecord(String petId, DailyWalkingRecordDTO dailyWalkingRecordDTO) {

        //DailyWalkingRecordDTO 에서 값 꺼내야함
        LocalDate recordDate = dailyWalkingRecordDTO.getRecordDate();
        Short targetWalkingTime = dailyWalkingRecordDTO.getTargetWalkingTime();
        Character walkingIntensity = dailyWalkingRecordDTO.getWalkingIntensity();
        Short walkingTime = dailyWalkingRecordDTO.getWalkingTime();

        if (recordDate == null || targetWalkingTime == null || walkingIntensity == null || walkingTime == null) {
            //recordDate, targetWalkingTime, walkingIntensity, walkingTime 중 적어도 하나가 null인 경우
            throw new InvalidInputException("All of 'recordDate', 'targetWalkingTime', 'walkingIntensity', and 'walkingTime' must be provided");
        }
        if (!recordDate.equals(LocalDate.now())) {
            //입력 날짜가 오늘 날짜가 아닌 경우
            throw new InvalidInputException("RecordDate must be today's date");
        }
        if (walkingIntensity != '강' && walkingIntensity != '중' && walkingIntensity != '하') {
            //walkingIntensity에 강/중/하 가 아닌 다른 값이 요청된 경우
            throw new InvalidInputException("WalkingIntensity must be 강/중/하");
        }

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null) {
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        if (!targetWalkingTime.equals(petEntity.getCurrentTargetWalkingTime())) {
            //targetWalkingTime이 현재 저장되어있는 current_target_walking_time 값이 아닌 경우
            throw new InvalidInputException("TargetWalkingTime must be same with current_target_walking_time in pets");
        }

        if (walkingRecordRepository.existsWalkingRecord(petId, recordDate)) {
            //이미 해당 날짜, petId로 산책 기록이 있는 경우
            throw new ConflictException("Daily-walking-record(" + recordDate + ") with PetID " + petId + " already exists");
        }

        //walking_records 테이블에 레코드 저장
        WalkingRecordEntity data = new WalkingRecordEntity();

        data.setWalkingRecordDate(recordDate);
        data.setTargetWalkingTime(targetWalkingTime);
        data.setWalkingIntensity(walkingIntensity);
        data.setWalkingTime(walkingTime);
        //target_walking_result 판단
        data.setTargetWalkingResult(walkingTime >= targetWalkingTime);
        //조회한 PetEntity를 WalkingRecordEntity의 반려동물 정보로 설정: pet_id
        data.setPet(petEntity);

        walkingRecordRepository.save(data);

        return true;
    }

    //daily-walking-record update api
    @Transactional
    public boolean updateDailyWalkingRecord(String petId, UpdateDailyWalkingRecordDTO updateDailyWalkingRecordDTO) {

        //UpdateDailyWalkingRecordDTO 에서 값 꺼내야함
        LocalDate recordDate = updateDailyWalkingRecordDTO.getRecordDate();
        Character walkingIntensity = updateDailyWalkingRecordDTO.getWalkingIntensity();
        Short walkingTime = updateDailyWalkingRecordDTO.getWalkingTime();

        if (recordDate == null || walkingIntensity == null || walkingTime == null) {
            //recordDate, walkingIntensity, walkingTime 중 적어도 하나가 null인 경우
            throw new InvalidInputException("All of 'recordDate', 'walkingIntensity', and 'walkingTime' must be provided");
        }
        if (walkingIntensity != '강' && walkingIntensity != '중' && walkingIntensity != '하') {
            //walkingIntensity에 강/중/하 가 아닌 다른 값이 요청된 경우
            throw new InvalidInputException("WalkingIntensity must be 강/중/하");
        }

        //해당 petId 있는지 확인
        if (!petRepository.existsByPetId(petId)){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //petId, recordDate로 저장된 산책 기록이 있는지 확인
        Optional<WalkingRecordEntity> dataWalkingRecord = walkingRecordRepository.findWalkingRecordByPetIdAndDate(petId, recordDate);
        if (dataWalkingRecord.isEmpty()) {
            //해당 날짜, petId로 산책 기록이 없는 경우
            throw new NotFoundException("Daily-walking-record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //walking_record update 진행
        WalkingRecordEntity data = dataWalkingRecord.get();
        data.setWalkingIntensity(walkingIntensity);
        data.setWalkingTime(walkingTime);
        data.setTargetWalkingResult(walkingTime >= data.getTargetWalkingTime());

        walkingRecordRepository.save(data);

        return true;
    }

    //daily-walking-record delete api
    @Transactional
    public boolean deleteDailyWalkingRecord(String petId, LocalDate recordDate) {

        //해당 petId 있는지 확인
        if (!petRepository.existsByPetId(petId)){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //petId, recordDate로 저장된 산책 기록이 있는지 확인
        Optional<WalkingRecordEntity> dataWalkingRecord = walkingRecordRepository.findWalkingRecordByPetIdAndDate(petId, recordDate);
        if (dataWalkingRecord.isEmpty()) {
            //해당 날짜, petId로 산책 기록이 없는 경우
            throw new NotFoundException("Daily-walking-record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //해당 날짜, petId로 산책 기록이 있는 경우 -> 해당 레코드 삭제
        WalkingRecordEntity data = dataWalkingRecord.get();
        walkingRecordRepository.delete(data);

        return true;
    }

    //daily-walking-record get api
    public DailyWalkingRecordResultDTO getDailyWalkingRecord(String petId, LocalDate recordDate) {

        //해당 petId 있는지 확인
        if (!petRepository.existsByPetId(petId)){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //petId, recordDate로 저장된 산책 기록이 있는지 확인
        Optional<WalkingRecordEntity> dataWalkingRecord = walkingRecordRepository.findWalkingRecordByPetIdAndDate(petId, recordDate);
        if (dataWalkingRecord.isEmpty()) {
            //해당 날짜, petId로 산책 기록이 없는 경우
            throw new NotFoundException("Daily-walking-record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //해당 날짜, petId로 산책 기록이 있는 경우
        WalkingRecordEntity data = dataWalkingRecord.get();

        //DailyWalkingRecordResultDTO 에 값 설정
        DailyWalkingRecordResultDTO dailyWalkingRecordResultDTO = new DailyWalkingRecordResultDTO();

        dailyWalkingRecordResultDTO.setRecordDate(data.getWalkingRecordDate());
        dailyWalkingRecordResultDTO.setTargetWalkingTime(data.getTargetWalkingTime());
        dailyWalkingRecordResultDTO.setWalkingIntensity(data.getWalkingIntensity());
        dailyWalkingRecordResultDTO.setWalkingTime(data.getWalkingTime());
        dailyWalkingRecordResultDTO.setTargetWalkingResult(data.getTargetWalkingResult());

        return dailyWalkingRecordResultDTO;
    }
}
