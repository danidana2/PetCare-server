package com.example.somserver.service;

import com.example.somserver.dto.CurrentWeightDTO;
import com.example.somserver.entity.CatAverageWeightEntity;
import com.example.somserver.entity.DogAverageWeightEntity;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.WeightRecordEntity;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.CatAverageWeightRepository;
import com.example.somserver.repository.DogAverageWeightRepository;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.WeightRecordRepository;
import com.example.somserver.utils.AnimalUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
}
