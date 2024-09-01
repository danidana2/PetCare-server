package com.example.somserver.service;

import com.example.somserver.dto.CurrentWeightDTO;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.WeightRecordEntity;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.WeightRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ObesityManagementService {

    //PetRepository, WeightRecordRepository 주입받기
    private final PetRepository petRepository;
    private final WeightRecordRepository weightRecordRepository;

    public ObesityManagementService(PetRepository petRepository, WeightRecordRepository weightRecordRepository) {

        this.petRepository = petRepository;
        this.weightRecordRepository = weightRecordRepository;
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

        //weightRecordRepository 한테 이 엔티티 값을 저장하는 메서드
        weightRecordRepository.save(dataWeightRecord);

        return true;
    }
}
