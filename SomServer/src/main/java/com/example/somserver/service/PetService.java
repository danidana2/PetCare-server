package com.example.somserver.service;

import com.example.somserver.dto.DailyRecordDTO;
import com.example.somserver.dto.PetProfileDTO;
import com.example.somserver.dto.UpdatePetDTO;
import com.example.somserver.entity.*;
import com.example.somserver.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class PetService {

    //PetRepository, WeightRecordRepository, PrescriptionRecordRepository, BloodSugarLevelRecordRepository, SpecialNoteRecordRepository 주입받기
    private final PetRepository petRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final PrescriptionRecordRepository prescriptionRecordRepository;
    private final BloodSugarLevelRecordRepository bloodSugarLevelRecordRepository;
    private final SpecialNoteRecordRepository specialNoteRecordRepository;

    public PetService(PetRepository petRepository, WeightRecordRepository weightRecordRepository, PrescriptionRecordRepository prescriptionRecordRepository, BloodSugarLevelRecordRepository bloodSugarLevelRecordRepository, SpecialNoteRecordRepository specialNoteRecordRepository) {

        this.petRepository = petRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.prescriptionRecordRepository = prescriptionRecordRepository;
        this.bloodSugarLevelRecordRepository = bloodSugarLevelRecordRepository;
        this.specialNoteRecordRepository = specialNoteRecordRepository;
    }

    //pet profile get api
    public PetProfileDTO getPetProfile(String petId) {

        //petId로 조회한 PetEntity에서 특정 정보 PetProfileDTO로 가져오기
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            return null;
        }

        PetProfileDTO petProfileDTO = new PetProfileDTO();
        petProfileDTO.setPetName(data.getPetName());
        petProfileDTO.setBreed(data.getBreed());
        petProfileDTO.setGender(data.getGender());
        petProfileDTO.setAge(data.getAge());
        petProfileDTO.setCurrentWeight(data.getCurrentWeight());
        petProfileDTO.setIsNeutered(data.getIsNeutered());
        petProfileDTO.setHasDiabetes(data.getHasDiabetes());

        return petProfileDTO;
    }

    //pet update api
    public boolean updatePet(String petId, UpdatePetDTO updatePetDTO) {

        //UpdatePetDTO 에서 값 꺼내야함
        String breed = updatePetDTO.getBreed();
        Byte age = updatePetDTO.getAge();
        BigDecimal currentWeight = updatePetDTO.getCurrentWeight();
        Boolean isNeutered = updatePetDTO.getIsNeutered();
        Character gender = updatePetDTO.getGender();
        Boolean hasDiabetes = updatePetDTO.getHasDiabetes();

        LocalTime insulinTime1 = updatePetDTO.getInsulinTime1();
        LocalTime insulinTime2 = updatePetDTO.getInsulinTime2();
        LocalTime insulinTime3 = updatePetDTO.getInsulinTime3();
        LocalDate heartwormShotDate = updatePetDTO.getHeartwormShotDate();
        LocalDate heartwormMedicineDate = updatePetDTO.getHeartwormMedicineDate();

        //pet update 진행: PetEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            return false;
        }

        if (breed != null) data.setBreed(breed);
        if (age != null) data.setAge(age);
        if (currentWeight != null) data.setCurrentWeight(currentWeight);
        if (isNeutered != null) data.setIsNeutered(isNeutered);
        if (gender != null) data.setGender(gender);
        if (hasDiabetes != null) data.setHasDiabetes(hasDiabetes);

        if (insulinTime1 != null) data.setInsulinTime1(insulinTime1);
        if (insulinTime2 != null) data.setInsulinTime2(insulinTime2);
        if (insulinTime3 != null) data.setInsulinTime3(insulinTime3);
        if (heartwormShotDate != null) data.setHeartwormShotDate(heartwormShotDate);
        if (heartwormMedicineDate != null) data.setHeartwormMedicineDate(heartwormMedicineDate);

        petRepository.save(data);

        /* currentWeight값 weight_records 테이블에 지금 날짜로 weight 기록 추가 */

        //currentWeight null 확인
        if (currentWeight != null) {
            //현재 Date
            LocalDate currentDate = LocalDate.now();

            //중복 petId, weightRecordDate 확인 -> WeightRecordRepository 에 메서드 작성 필요
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
                return false;
            }
            dataWeightRecord.setPet(petEntity);

            //weightRecordRepository 한테 이 엔티티 값을 저장하는 메서드
            weightRecordRepository.save(dataWeightRecord);

            return true;
        }

        return true;
    }

    //pet delete api
    public boolean deletePet(String petId) {

        //petId로 조회한 PetEntity DB에서 삭제
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            return false;
        }

        petRepository.delete(data);

        return true;
    }

    //daily-record add api
    @Transactional
    public String addDailyRecord(String petId, DailyRecordDTO dailyRecordDTO) {

        //DailyRecordDTO 에서 값 꺼내야함
        LocalDate recordDate = dailyRecordDTO.getRecordDate();
        String diagnosis = dailyRecordDTO.getDiagnosis();
        String medicine = dailyRecordDTO.getMedicine();
        BigDecimal weight = dailyRecordDTO.getWeight();
        Short bloodSugarLevel = dailyRecordDTO.getBloodSugarLevel();
        String specialNote = dailyRecordDTO.getSpecialNote();

        //PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            return "notFound";
        }

        //해당 "petId", "recordDate"로 이미 레코드가 있는지 확인
        Optional<PrescriptionRecordEntity> existingPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> existingWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> existingBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> existingSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        if (existingPrescriptionRecord.isPresent() || existingWeightRecord.isPresent() || existingBloodSugarLevelRecord.isPresent() || existingSpecialNoteRecord.isPresent()) {
            return "isExist";
        }

        //“diagnosis”, “weight”, “bloodSugarLevel”, “specialNote” 중 적어도 한가지 필수 입력 확인
        if (diagnosis == null && weight == null && bloodSugarLevel == null && specialNote == null) {
            return "notValid";
        }

        //“medicine”에 값을 입력하는 경우 무조건 “diagnosis”에도 값을 필수 입력
        if (medicine != null && diagnosis == null) {
            return "notValid";
        }

        //날짜별 처방전(진단명, 처방약), 몸무게, 혈당, 특이사항 각 DB의 해당 테이블에 값 저장
        boolean weightAdded = false;
        boolean bloodSugarLevelAdded = false;

        if (diagnosis != null) {
            //처방전(진단명, 처방약) 저장
            //prescription_record create 진행: PrescriptionRecordEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            PrescriptionRecordEntity data = new PrescriptionRecordEntity();

            data.setPrescriptionRecordDate(recordDate);
            data.setDiagnosis(diagnosis);
            data.setMedicine(medicine);
            //조회한 PetEntity를 PrescriptionRecordEntity의 반려동물 정보로 설정: pet_id
            data.setPet(petEntity);

            prescriptionRecordRepository.save(data);
        }
        if (weight != null) {
            //몸무게 저장
            //weight_record create 진행: WeightRecordEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            WeightRecordEntity data = new WeightRecordEntity();

            data.setWeightRecordDate(recordDate);
            data.setWeight(weight);
            //조회한 PetEntity를 WeightRecordEntity의 반려동물 정보로 설정: pet_id
            data.setPet(petEntity);

            weightRecordRepository.save(data);
            weightAdded = true;
        }
        if (bloodSugarLevel != null) {
            //혈당 저장
            //blood_sugar_level_record create 진행: BloodSugarLevelRecordEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            BloodSugarLevelRecordEntity data = new BloodSugarLevelRecordEntity();

            data.setSugarRecordDate(recordDate);
            data.setBloodSugarLevel(bloodSugarLevel);
            //조회한 PetEntity를 BloodSugarLevelRecordEntity의 반려동물 정보로 설정: pet_id
            data.setPet(petEntity);

            bloodSugarLevelRecordRepository.save(data);
            bloodSugarLevelAdded = true;
        }
        if (specialNote != null) {
            //특이사항 저장
            //specialNote create 진행: SpecialNoteRecordEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            SpecialNoteRecordEntity data = new SpecialNoteRecordEntity();

            data.setSpecialNoteRecordDate(recordDate);
            data.setSpecialNote(specialNote);
            //조회한 PetEntity를 SpecialNoteRecordEntity의 반려동물 정보로 설정: pet_id
            data.setPet(petEntity);

            specialNoteRecordRepository.save(data);
        }

        //pets 테이블에 current_weight, current_blood_sugar_level 갑 update
        if (weightAdded) { //weightAdded: true
            //pets 테이블 current_weight update
            WeightRecordEntity weightRecordEntity = weightRecordRepository.findFirstByPetPetIdOrderByWeightRecordDateDesc(petId);
            if (weightRecordEntity != null) {
                //가장 최신의 weight 값 조회된 경우
                BigDecimal currentWeight = weightRecordEntity.getWeight();
                petEntity.setCurrentWeight(currentWeight);
            } else {
                return "notFound";
            }
        }
        if (bloodSugarLevelAdded) { //bloodSugarLevelAdded: true
            //pets 테이블 current_blood_sugar_level update
            BloodSugarLevelRecordEntity bloodSugarLevelRecordEntity = bloodSugarLevelRecordRepository.findFirstByPetPetIdOrderBySugarRecordDateDesc(petId);
            if (bloodSugarLevelRecordEntity != null) {
                //가장 최신의 blood_sugar_level 값 조회된 경우
                Short currentBloodSugarLevel = bloodSugarLevelRecordEntity.getBloodSugarLevel();
                petEntity.setCurrentBloodSugarLevel(currentBloodSugarLevel);
            } else {
                return "notFound";
            }
        }
        if (weightAdded || bloodSugarLevelAdded) {
            petRepository.save(petEntity);
        }

        return "success";
    }

    //daily-record update api
    @Transactional
    public String updateDailyRecord(String petId, DailyRecordDTO dailyRecordDTO) {
        //DailyRecordDTO 에서 값 꺼내야함
        LocalDate recordDate = dailyRecordDTO.getRecordDate();
        String diagnosis = dailyRecordDTO.getDiagnosis();
        String medicine = dailyRecordDTO.getMedicine();
        BigDecimal weight = dailyRecordDTO.getWeight();
        Short bloodSugarLevel = dailyRecordDTO.getBloodSugarLevel();
        String specialNote = dailyRecordDTO.getSpecialNote();

        //PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            return "notFound";
        }

        //해당 "petId", "recordDate"로 이미 레코드 조회
        Optional<PrescriptionRecordEntity> existingPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> existingWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> existingBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> existingSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        if (existingPrescriptionRecord.isEmpty() && existingWeightRecord.isEmpty() && existingBloodSugarLevelRecord.isEmpty() && existingSpecialNoteRecord.isEmpty()) {
            return "notFound";
        }

        //“diagnosis”, “medicine”, “weight”, “bloodSugarLevel”, “specialNote” 중 적어도 한가지 필수 입력 확인
        if (diagnosis == null && medicine == null && weight == null && bloodSugarLevel == null && specialNote == null) {
            return "notValid";
        }

        //해당 petId와 날짜의 처방전(진단명, 처방약), 몸무게, 혈당, 특이사항 각 DB의 해당 테이블에 값 update
        boolean weightUpdated = false;
        boolean bloodSugarLevelUpdated = false;

        if (diagnosis == null && medicine != null) {
            //처방전(진단명, 처방약) 업데이트
            PrescriptionRecordEntity data;

            if (existingPrescriptionRecord.isPresent()) {
                //이미 저장된 처방전 기록이 있는 경우: 업데이트
                data = existingPrescriptionRecord.get();

                data.setMedicine(medicine);
            } else {
                return "notValid";
            }

            prescriptionRecordRepository.save(data);
        }
        if (diagnosis != null) {
            //처방전(진단명, 처방약) 업데이트 또는 생성
            PrescriptionRecordEntity data;

            if (existingPrescriptionRecord.isPresent()) {
                //이미 저장된 처방전 기록이 있는 경우: 업데이트
                data = existingPrescriptionRecord.get();

                data.setDiagnosis(diagnosis);
                if (medicine != null) {
                    data.setMedicine(medicine);
                }
            } else {
                //이미 저장된 처방전 기록이 없는 경우: 생성
                data = new PrescriptionRecordEntity();

                data.setPrescriptionRecordDate(recordDate);
                data.setDiagnosis(diagnosis);
                data.setMedicine(medicine);
                //조회한 PetEntity를 PrescriptionRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);
            }

            prescriptionRecordRepository.save(data);
        }
        if (weight != null) {
            //몸무게 업데이트 또는 생성
            WeightRecordEntity data;

            if (existingWeightRecord.isPresent()) {
                //이미 저장된 몸무게 기록이 있는 경우: 업데이트
                data = existingWeightRecord.get();

                data.setWeight(weight);
            } else {
                //이미 저장된 몸무게 기록이 없는 경우: 생성
                data = new WeightRecordEntity();

                data.setWeightRecordDate(recordDate);
                data.setWeight(weight);
                //조회한 PetEntity를 WeightRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);
            }

            weightRecordRepository.save(data);
            weightUpdated = true;
        }
        if (bloodSugarLevel != null) {
            //혈당 업데이트 또는 생성
            BloodSugarLevelRecordEntity data;

            if (existingBloodSugarLevelRecord.isPresent()) {
                //이미 저장된 혈당 기록이 있는 경우: 업데이트
                data = existingBloodSugarLevelRecord.get();

                data.setBloodSugarLevel(bloodSugarLevel);
            } else {
                //이미 저장된 혈당 기록이 없는 경우: 생성
                data = new BloodSugarLevelRecordEntity();

                data.setSugarRecordDate(recordDate);
                data.setBloodSugarLevel(bloodSugarLevel);
                //조회한 PetEntity를 BloodSugarLevelRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);
            }

            bloodSugarLevelRecordRepository.save(data);
            bloodSugarLevelUpdated = true;
        }
        if (specialNote != null) {
            //특이사항 업데이트 또는 생성
            SpecialNoteRecordEntity data;

            if (existingSpecialNoteRecord.isPresent()) {
                //이미 저장된 특이사항 기록이 있는 경우: 업데이트
                data = existingSpecialNoteRecord.get();

                data.setSpecialNote(specialNote);
            } else {
                //이미 저장된 특이사항 기록이 없는 경우: 생성
                data = new SpecialNoteRecordEntity();

                data.setSpecialNoteRecordDate(recordDate);
                data.setSpecialNote(specialNote);
                //조회한 PetEntity를 SpecialNoteRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);
            }

            specialNoteRecordRepository.save(data);
        }

        //pets 테이블에 current_weight, current_blood_sugar_level 갑 update
        if (weightUpdated) { //weightAdded: true
            //pets 테이블 current_weight update
            WeightRecordEntity weightRecordEntity = weightRecordRepository.findFirstByPetPetIdOrderByWeightRecordDateDesc(petId);
            if (weightRecordEntity != null) {
                //가장 최신의 weight 값 조회된 경우
                BigDecimal currentWeight = weightRecordEntity.getWeight();
                petEntity.setCurrentWeight(currentWeight);
            } else {
                return "notFound";
            }
        }
        if (bloodSugarLevelUpdated) { //bloodSugarLevelAdded: true
            //pets 테이블 current_blood_sugar_level update
            BloodSugarLevelRecordEntity bloodSugarLevelRecordEntity = bloodSugarLevelRecordRepository.findFirstByPetPetIdOrderBySugarRecordDateDesc(petId);
            if (bloodSugarLevelRecordEntity != null) {
                //가장 최신의 blood_sugar_level 값 조회된 경우
                Short currentBloodSugarLevel = bloodSugarLevelRecordEntity.getBloodSugarLevel();
                petEntity.setCurrentBloodSugarLevel(currentBloodSugarLevel);
            } else {
                return "notFound";
            }
        }
        if (weightUpdated || bloodSugarLevelUpdated) {
            petRepository.save(petEntity);
        }

        return "success";
    }
}
