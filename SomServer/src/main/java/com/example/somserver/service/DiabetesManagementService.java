package com.example.somserver.service;

import com.example.somserver.dto.CalculateDerCaloriesDTO;
import com.example.somserver.dto.DiabetesRiskCheckDTO;
import com.example.somserver.dto.DiabetesRiskDTO;
import com.example.somserver.dto.DiabetesRiskResultDTO;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;

@Service
public class DiabetesManagementService {

    //PetRepository 주입받기
    private final PetRepository petRepository;

    public DiabetesManagementService(PetRepository petRepository) {

        this.petRepository = petRepository;
    }

    //diabetes-risk check api
    @Transactional
    public DiabetesRiskResultDTO checkDiabetesRisk(String petId, DiabetesRiskCheckDTO diabetesRiskCheckDTO) {

        //DiabetesRiskCheckDTO 에서 값 꺼내야함
        Character isObesity = diabetesRiskCheckDTO.getIsObesity();
        String dailyWaterIntake = diabetesRiskCheckDTO.getDailyWaterIntake();
        String foodIntake = diabetesRiskCheckDTO.getFoodIntake();
        Character isWeightLoss = diabetesRiskCheckDTO.getIsWeightLoss();
        Character isIncreasedUrination = diabetesRiskCheckDTO.getIsIncreasedUrination();

        //DiabetesRiskResultDTO 에 넣을 결과값 초기 설정
        String petName = null;
        String diabetesRisk = null;
        String recommendedNote = null;

        //해당 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 반려동물 이름 정보 가져오기
        petName = data.getPetName();

        //diaPoint, obs, der 변수 초기설정
        int diaPoint = 0;
        boolean obs = false;

        //Q1. 비만인가요?
        if (isObesity.equals('y')) {
            diaPoint += 1;
            obs = true;
        } else if (isObesity.equals('n')) {

        } else {
            throw new InvalidInputException("Invalid input data for diabetes-risk check");
        }

        //Q2. 물을 하루에 (평균음수량)ml 이상/동일/이하 (으)로 마십니다.
        if (dailyWaterIntake.equals("이상")) {
            diaPoint += 1;
        } else if (dailyWaterIntake.equals("동일") || dailyWaterIntake.equals("이하")) {

        } else {
            throw new InvalidInputException("Invalid input data for diabetes-risk check");
        }

        //Q3. 식사량 (der 칼로리) 기준으로 많음/보통/적음 입니다.
        if (foodIntake.equals("많음")) {
            diaPoint += 1;
        } else if (foodIntake.equals("보통") || foodIntake.equals("적음")) {

        } else {
            throw new InvalidInputException("Invalid input data for diabetes-risk check");
        }

        //Q4. 최근 먹는 양에 비해서 체중이 감소했습니까?
        if (isWeightLoss.equals('y')) {
            diaPoint += 1;
        } else if (isWeightLoss.equals('n')) {

        } else {
            throw new InvalidInputException("Invalid input data for diabetes-risk check");
        }

        //Q5. 최근 소변량이 증가하고 소변을 자주 봅니까?
        if (isIncreasedUrination.equals('y')) {
            diaPoint += 1;
        } else if (isIncreasedUrination.equals('n')) {

        } else {
            throw new InvalidInputException("Invalid input data for diabetes-risk check");
        }

        //diaPoint 결과값에 따른 당뇨 위험도 분류
        if (diaPoint >= 4) {
            diabetesRisk = "당뇨 의심";
            recommendedNote = "병원 방문 및 당뇨 검사 권유";
        } else if (diaPoint >= 2 && diaPoint < 4) {
            diabetesRisk = "당뇨 위험 보통";
            if (obs) {
                recommendedNote = "체중 감량 및 운동 추천";
            } else {
                recommendedNote = "하루 적정 칼로리, 음수량 지키기";
            }
        } else if (diaPoint < 2 ) {
            diabetesRisk = "당뇨 위험 없음";
        }

        //pets 테이블에 값 저장
        data.setDiabetesRisk(diabetesRisk);
        data.setDiabetesRiskCheckDate(LocalDate.now());

        petRepository.save(data);

        //DiabetesRiskResultDTO 에 결과값 설정
        DiabetesRiskResultDTO diabetesRiskResultDTO = new DiabetesRiskResultDTO();

        diabetesRiskResultDTO.setPetName(petName);
        diabetesRiskResultDTO.setDiabetesRisk(diabetesRisk);
        diabetesRiskResultDTO.setRecommendedNote(recommendedNote);

        return diabetesRiskResultDTO;
    }

    //daily-water-intake get api
    public BigDecimal getDailyWaterIntake(String petId) {

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //하루 평균 음수량 계산
        BigDecimal currentWeight = petEntity.getCurrentWeight();
        double weight = currentWeight.doubleValue();
        double dailyWaterIntake = Math.pow(weight, 0.75) * 132;

        return BigDecimal.valueOf(dailyWaterIntake);
    }

    //der-calories calculate api
    public BigDecimal calculateDerCalories(String petId, CalculateDerCaloriesDTO calculateDerCaloriesDTO) {

        //CalculateDerCaloriesDTO 에서 값 꺼내야함
        Character isObesity = calculateDerCaloriesDTO.getIsObesity();

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //der 계산
        BigDecimal currentWeight = petEntity.getCurrentWeight();
        double weight = currentWeight.doubleValue();
        double rer = Math.pow(weight, 0.75) * 70;
        double der = 0;

        if (isObesity.equals('y')) {
            der = rer * 1.3;
        } else if (isObesity.equals('n')) {
            der = rer * 2.0;
        }
        
        return BigDecimal.valueOf(der);
    }

    //diabetes-risk get api
    public DiabetesRiskDTO getDiabetesRisk(String petId) {

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //DiabetesRiskDTO 에 넣을 결과값 설정
        DiabetesRiskDTO diabetesRiskDTO = new DiabetesRiskDTO();

        diabetesRiskDTO.setDiabetesRiskCheckDate(petEntity.getDiabetesRiskCheckDate());
        diabetesRiskDTO.setDiabetesRisk(petEntity.getDiabetesRisk());

        return diabetesRiskDTO;
    }
}
