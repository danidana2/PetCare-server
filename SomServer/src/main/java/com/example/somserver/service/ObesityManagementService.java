package com.example.somserver.service;

import com.example.somserver.dto.*;
import com.example.somserver.entity.CatAverageWeightEntity;
import com.example.somserver.entity.DogAverageWeightEntity;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.WeightRecordEntity;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.CatAverageWeightRepository;
import com.example.somserver.repository.DogAverageWeightRepository;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.WeightRecordRepository;
import com.example.somserver.utils.AnimalUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ObesityManagementService {

    //PetRepository, WeightRecordRepository, CatAverageWeightRepository, DogAverageWeightRepository 주입받기
    private final PetRepository petRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final CatAverageWeightRepository catAverageWeightRepository;
    private final DogAverageWeightRepository dogAverageWeightRepository;

    public ObesityManagementService(PetRepository petRepository, WeightRecordRepository weightRecordRepository, CatAverageWeightRepository catAverageWeightRepository, DogAverageWeightRepository dogAverageWeightRepository) {

        this.petRepository = petRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.catAverageWeightRepository = catAverageWeightRepository;
        this.dogAverageWeightRepository = dogAverageWeightRepository;
    }

    //current-weight get api
    public BigDecimal getCurrentWeight(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return petEntity.getCurrentWeight();
    }

    //current-weight update api
    @Transactional
    public boolean updateCurrentWeight(String petId, CurrentWeightDTO currentWeightDTO) {

        //CurrentWeightDTO 에서 값 꺼내야함
        BigDecimal currentWeight = currentWeightDTO.getCurrentWeight();

        //pet update 진행: PetEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }
        data.setCurrentWeight(currentWeight);

        petRepository.save(data);

        /* currentWeight값 weight_records 테이블에 지금 날짜로 weight 기록 추가 */

        //현재 Date
        LocalDate currentDate = LocalDate.now();

        //중복 petId, weightRecordDate 확인
        Optional<WeightRecordEntity> existingWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, currentDate);

        //pet_id와 weight_record_date가 일치하는 레코드 존재하는 경우
        if (existingWeightRecord.isPresent()){
            WeightRecordEntity dataWeightRecord = existingWeightRecord.get();
            dataWeightRecord.setWeight(currentWeight);

            weightRecordRepository.save(dataWeightRecord);

            return true;
        }

        //pet_id와 weight_record_date가 일치하는 레코드 존재하지 않는 경우
        WeightRecordEntity dataWeightRecord = new WeightRecordEntity();

        dataWeightRecord.setWeight(currentWeight);
        dataWeightRecord.setWeightRecordDate(currentDate);

        //조회한 PetEntity를 WeightRecordEntity의 반려동물 정보로 설정: pet_id
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }
        dataWeightRecord.setPet(petEntity);

        weightRecordRepository.save(dataWeightRecord);

        return true;
    }

    //standard-weight check api
    public String checkStandardWeight(String petId) {

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //조회된 PetEntity에서 현재 몸무게 가져오기
        BigDecimal currentWeight = petEntity.getCurrentWeight();

        //해당 petId의 정보에 맞는 표준 몸무게 범위 가져오기
        BigDecimal averageWeightMin = null;
        BigDecimal averageWeightMax = null;

        String breed = petEntity.getBreed();
        String animalType = AnimalUtils.getAnimalType(breed);

        if (animalType.equals("Cat")) {
            //cat_average_weights 테이블에서 해당 cat_breed의 cat_average_weight_min, cat_average_weight_max 값 가져오기
            CatAverageWeightEntity catAverageWeightEntity = catAverageWeightRepository.findByCatBreed(breed);

            averageWeightMin = catAverageWeightEntity.getCatAverageWeightMin();
            averageWeightMax = catAverageWeightEntity.getCatAverageWeightMax();
        } else if (animalType.equals("Dog")) {
            //dog_average_weights 테이블에서 해당 dog_breed, dog_is_neutered, dog_gender의 dog_average_weight_min, dog_average_weight_max 값 가져오기
            Boolean isNeutered = petEntity.getIsNeutered();
            Character gender = petEntity.getGender();
            DogAverageWeightEntity dogAverageWeightEntity = dogAverageWeightRepository.findByDogBreedAndDogIsNeuteredAndDogGender(breed, isNeutered, gender);

            averageWeightMin = dogAverageWeightEntity.getDogAverageWeightMin();
            averageWeightMax = dogAverageWeightEntity.getDogAverageWeightMax();
        }

        //현재 몸무게와 표준 몸무게 비교
        int compareToMax = currentWeight.compareTo(averageWeightMax);
        int compareToMin = currentWeight.compareTo(averageWeightMin);

        if (compareToMax > 0) {
            return "over";
        } else if (compareToMin < 0) {
            return "under";
        } else {
            return "average";
        }
    }

    //obesity-degree check -> daily-calorie calculate api
    @Transactional
    public DailyCalorieResultDTO calculateDailyCalorie(String petId, DailyCalorieCheckDTO dailyCalorieCheckDTO) {

        //DailyCalorieCheckDTO 에서 값 꺼내야함
        String weightStatus = dailyCalorieCheckDTO.getWeightStatus();
        Character waistRibVisibility = dailyCalorieCheckDTO.getWaistRibVisibility();
        Character ribTouchability = dailyCalorieCheckDTO.getRibTouchability();
        Integer bodyShape = dailyCalorieCheckDTO.getBodyShape();
        String activityLevel = dailyCalorieCheckDTO.getActivityLevel();

        //DailyCalorieResultDTO 에 넣을 결과값 초기 설정
        String petName = null;
        String obesityDegree = null;
        BigDecimal recommendedCalories = null;

        //해당 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 반려동물 이름 정보 가져오기
        petName = data.getPetName();

        //해당 반려동물 현재 몸무게 정보 가져오기
        BigDecimal currentWeight = data.getCurrentWeight();
        double weight = currentWeight.doubleValue();

        //rer 계산
        double rer = Math.pow(weight, 0.75) * 70;

        //fatPoint, der 변수 초기설정
        int fatPoint = 0;
        double der = 0;
        double derConstant = 0;

        //Q1. 평균 체중보다 이상/이하/평균 입니다.
        switch (weightStatus) {
            case "average" -> {
                //비만도
                obesityDegree = "정상";

                //Q5. 운동량이 많음/보통/적음 입니다.
                derConstant = switch (activityLevel) {
                    case "많음" ->
                            // der 계수
                            3.5;
                    case "보통" ->
                            // der 계수
                            2.0;
                    case "적음" ->
                            // der 계수
                            1.3;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                };
            }
            case "over" -> {
                //fatPoint
                fatPoint += 4;

                //Q2. 허리나 갈비뼈가 보입니까?
                switch (waistRibVisibility) {
                    case 'y' -> {
                    }
                    case 'n' ->
                            //fatPoint
                            fatPoint += 1;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }

                //Q3. 허리를 만졌을 때 갈비뼈가 만져지나요?
                switch (ribTouchability) {
                    case 'y' -> {
                    }
                    case 'n' ->
                            //fatPoint
                            fatPoint += 1;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }

                //Q4. 체형 선택지 선택
                switch (bodyShape) {
                    case 1 -> {
                    }
                    case 2 ->
                            //fatPoint
                            fatPoint += 1;
                    case 3 ->
                            //fatPoint
                            fatPoint += 2;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }
            }
            case "under" -> {
                //Q2. 허리나 갈비뼈가 보입니까?
                switch (waistRibVisibility) {
                    case 'y' -> {
                    }
                    case 'n' ->
                            //fatPoint
                            fatPoint += 1;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }

                //Q3. 허리를 만졌을 때 갈비뼈가 만져지나요?
                switch (ribTouchability) {
                    case 'y' -> {
                    }
                    case 'n' ->
                            //fatPoint
                            fatPoint += 1;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }

                //Q4. 체형 선택지 선택
                switch (bodyShape) {
                    case 1 -> {
                    }
                    case 2 ->
                            //fatPoint
                            fatPoint += 1;
                    case 3 ->
                            //fatPoint
                            fatPoint += 2;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                }
            }
            default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
        }

        //fatPoint에 따라 분류
        if (weightStatus.equals("over") || weightStatus.equals("under")) {
            if (fatPoint == 3 || fatPoint == 4) {
                //비만도
                obesityDegree = "정상";

                //Q5. 운동량이 많음/보통/적음 입니다.
                derConstant = switch (activityLevel) {
                    case "많음" ->
                            // der 계수
                            3.5;
                    case "보통" ->
                            // der 계수
                            2.0;
                    case "적음" ->
                            // der 계수
                            1.3;
                    default -> throw new InvalidInputException("Invalid input data for daily calorie calculation");
                };
            } else if (fatPoint == 2) {
                //비만도
                obesityDegree = "저체중";

                //der 계수
                derConstant = 1.4;
            } else if (fatPoint == 1 || fatPoint == 0) {
                //비만도
                obesityDegree = "저체중";

                //der 계수
                derConstant = 1.8;
            } else if (fatPoint > 4) {
                //비만도
                obesityDegree = "과체중";

                //der 계수
                derConstant = 1.0;
            }
        }

        //der 계산
        der = derConstant * rer;
        recommendedCalories = BigDecimal.valueOf(der);
        recommendedCalories = recommendedCalories.setScale(2, RoundingMode.HALF_UP);

        //pets 테이블에 값 저장(따로 api 생성)
        //data.setObesityDegree(obesityDegree);
        //data.setRecommendedCalories(recommendedCalories);
        //data.setWeightCalRecommendedCalories(currentWeight);
        //data.setCalRecommendedCaloriesDate(LocalDate.now());
        //petRepository.save(data);

        //DailyCalorieResultDTO 에 결과값 설정
        DailyCalorieResultDTO dailyCalorieResultDTO = new DailyCalorieResultDTO();

        dailyCalorieResultDTO.setPetName(petName);
        dailyCalorieResultDTO.setObesityDegree(obesityDegree);
        dailyCalorieResultDTO.setRecommendedCalories(recommendedCalories);
        dailyCalorieResultDTO.setWeightCalRecommendedCalories(currentWeight);
        dailyCalorieResultDTO.setCalRecommendedCaloriesDate(LocalDate.now());

        return dailyCalorieResultDTO;
    }

    //obesity-degree, daily-calorie update api
    @Transactional
    public boolean updateDailyCalorieResult(String petId, UpdateDailyCalorieResultDTO updateDailyCalorieResultDTO) {

        //UpdateDailyCalorieResultDTO 에서 값 꺼내야함
        String obesityDegree = updateDailyCalorieResultDTO.getObesityDegree();
        BigDecimal recommendedCalories = updateDailyCalorieResultDTO.getRecommendedCalories();
        BigDecimal weightCalRecommendedCalories = updateDailyCalorieResultDTO.getWeightCalRecommendedCalories();
        LocalDate calRecommendedCaloriesDate = updateDailyCalorieResultDTO.getCalRecommendedCaloriesDate();

        if (obesityDegree == null || recommendedCalories == null || weightCalRecommendedCalories == null || calRecommendedCaloriesDate == null) {
            //UpdateDailyCalorieResultDTO 에서 적어도 하나가 null 값인 경우
            throw new InvalidInputException("At least one of 'obesityDegree', 'recommendedCalories', 'weightCalRecommendedCalories', or 'calRecommendedCaloriesDate' is null");
        }

        if (!obesityDegree.equals("정상") && !obesityDegree.equals("저체중") && !obesityDegree.equals("과체중")) {
            //obesityDegree 값이 정상, 저체중, 과체중 이 아닌 값인 경우
            throw new InvalidInputException("Invalid input data for obesityDegree");

        }

        //해당 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //pets 테이블에 값 저장
        data.setObesityDegree(obesityDegree);
        data.setRecommendedCalories(recommendedCalories);
        data.setWeightCalRecommendedCalories(weightCalRecommendedCalories);
        data.setCalRecommendedCaloriesDate(calRecommendedCaloriesDate);

        petRepository.save(data);

        return true;
    }

    //daily-calorie get api
    public DailyCalorieDTO getDailyCalorie(String petId) {

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //DailyCalorieDTO 에 결과값 설정
        DailyCalorieDTO dailyCalorieDTO = new DailyCalorieDTO();

        dailyCalorieDTO.setCalRecommendedCaloriesDate(petEntity.getCalRecommendedCaloriesDate());
        dailyCalorieDTO.setRecommendedCalories(petEntity.getRecommendedCalories());

        return dailyCalorieDTO;
    }

    //weight-cal-recommended-calories get api
    public BigDecimal getWeightCalRecommendedCalories(String petId) {

        //해당 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return petEntity.getWeightCalRecommendedCalories();
    }
}
