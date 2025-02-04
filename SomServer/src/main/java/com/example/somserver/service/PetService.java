package com.example.somserver.service;

import com.example.somserver.dto.*;
import com.example.somserver.entity.*;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.*;
import com.example.somserver.utils.AnimalUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        PetProfileDTO petProfileDTO = new PetProfileDTO();

        petProfileDTO.setPetName(data.getPetName());
        petProfileDTO.setBreed(data.getBreed());
        petProfileDTO.setGender(data.getGender());
        petProfileDTO.setAge(data.getAge());
        petProfileDTO.setCurrentWeight(data.getCurrentWeight());
        petProfileDTO.setIsNeutered(data.getIsNeutered());
        petProfileDTO.setHasDiabetes(data.getHasDiabetes());

        petProfileDTO.setInsulinTime1(data.getInsulinTime1());
        petProfileDTO.setInsulinTime2(data.getInsulinTime2());
        petProfileDTO.setInsulinTime3(data.getInsulinTime3());
        petProfileDTO.setHeartwormShotDate(data.getHeartwormShotDate());
        petProfileDTO.setHeartwormMedicineDate(data.getHeartwormMedicineDate());

        return petProfileDTO;
    }

    //pet profile summary get api
    public PetProfileSummaryDTO getPetProfileSummary(String petId) {

        //petId로 조회한 PetEntity에서 특정 정보 PetProfileSummaryDTO로 가져오기
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        PetProfileSummaryDTO petProfileSummaryDTO = new PetProfileSummaryDTO();
        petProfileSummaryDTO.setPetName(data.getPetName());
        petProfileSummaryDTO.setAge(data.getAge());
        petProfileSummaryDTO.setCurrentWeight(data.getCurrentWeight());
        petProfileSummaryDTO.setAnimalType(AnimalUtils.getAnimalType(data.getBreed()));

        return petProfileSummaryDTO;
    }

    //pet update api
    @Transactional
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
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        if (breed == null || age == null || currentWeight == null || isNeutered == null || gender == null || hasDiabetes == null) {
            //프론트에서 잘못된 요청 값
            throw new InvalidInputException("At least one of 'breed', 'age', 'currentWeight', 'isNeutered', 'gender', or 'hasDiabetes' not entered");
        }
        data.setBreed(breed);
        data.setAge(age);
        data.setCurrentWeight(currentWeight);
        data.setIsNeutered(isNeutered);
        data.setGender(gender);
        data.setHasDiabetes(hasDiabetes);

        //insulin_time1,2,3 앞에 칸부터 입력되도록 수정
        if (insulinTime1 == null && insulinTime2 == null && insulinTime3 != null) {
            insulinTime1 = insulinTime3;
            insulinTime3 = null;
        } else if (insulinTime1 == null && insulinTime2 != null && insulinTime3 == null) {
            insulinTime1 = insulinTime2;
            insulinTime2 = null;
        } else if (insulinTime1 == null && insulinTime2 != null && insulinTime3 != null) {
            insulinTime1 = insulinTime2;
            insulinTime2 = insulinTime3;
            insulinTime3 = null;
        } else if (insulinTime1 != null && insulinTime2 == null && insulinTime3 != null) {
            insulinTime2 = insulinTime3;
            insulinTime3 = null;
        }

        data.setInsulinTime1(insulinTime1);
        data.setInsulinTime2(insulinTime2);
        data.setInsulinTime3(insulinTime3);
        data.setHeartwormShotDate(heartwormShotDate);
        data.setHeartwormMedicineDate(heartwormMedicineDate);

        petRepository.save(data);

        /* currentWeight값 weight_records 테이블에 지금 날짜로 weight 기록 추가 */

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
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }
        dataWeightRecord.setPet(petEntity);

        weightRecordRepository.save(dataWeightRecord);

        return true;
    }

    //pet delete api
    @Transactional
    public boolean deletePet(String petId) {

        //petId로 조회한 PetEntity DB에서 삭제
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        petRepository.delete(data);

        return true;
    }

    //daily-record add api
    @Transactional
    public boolean addDailyRecord(String petId, DailyRecordDTO dailyRecordDTO) {

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
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 "petId", "recordDate"로 이미 레코드가 있는지 확인
        Optional<PrescriptionRecordEntity> existingPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> existingWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> existingBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> existingSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        if (existingPrescriptionRecord.isPresent() || existingWeightRecord.isPresent() || existingBloodSugarLevelRecord.isPresent() || existingSpecialNoteRecord.isPresent()) {
            throw new ConflictException("Daily-Record(" + recordDate + ") with PetID " + petId + " already exists");
        }

        //“medicine”에 값을 입력하는 경우 무조건 “diagnosis”에도 값을 필수 입력
        if (medicine != null && diagnosis == null) {
            throw new InvalidInputException("A value for 'diagnosis' is required when 'medicine' is entered");
        }

        //“diagnosis”, “weight”, “bloodSugarLevel”, “specialNote” 중 적어도 한가지 필수 입력 확인
        if (diagnosis == null && weight == null && bloodSugarLevel == null && specialNote == null) {
            throw new InvalidInputException("At least one of 'diagnosis', 'weight', 'bloodSugarLevel', or 'specialNote' must be provided");
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
            }
        }
        if (bloodSugarLevelAdded) { //bloodSugarLevelAdded: true
            //pets 테이블 current_blood_sugar_level update
            BloodSugarLevelRecordEntity bloodSugarLevelRecordEntity = bloodSugarLevelRecordRepository.findFirstByPetPetIdOrderBySugarRecordDateDesc(petId);
            if (bloodSugarLevelRecordEntity != null) {
                //가장 최신의 blood_sugar_level 값 조회된 경우
                Short currentBloodSugarLevel = bloodSugarLevelRecordEntity.getBloodSugarLevel();
                petEntity.setCurrentBloodSugarLevel(currentBloodSugarLevel);
            }
        }
        if (weightAdded || bloodSugarLevelAdded) {
            petRepository.save(petEntity);
        }

        return true;
    }

    //daily-record update api
    @Transactional
    public boolean updateDailyRecord(String petId, DailyRecordDTO dailyRecordDTO) {

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
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 "petId", "recordDate"로 레코드 조회
        Optional<PrescriptionRecordEntity> dataPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> dataWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> dataBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> dataSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        //해당 "petId", "recordDate"로 처방전(진단명, 처방약), 몸무게, 혈당, 특이사항 기록 레코드가 모두 없는 경우
        if (dataPrescriptionRecord.isEmpty() && dataWeightRecord.isEmpty() && dataBloodSugarLevelRecord.isEmpty() && dataSpecialNoteRecord.isEmpty()) {
            throw new NotFoundException("Daily-Record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //“medicine”에 값을 입력하는 경우 무조건 “diagnosis”에도 값을 필수 입력
        if (medicine != null && diagnosis == null) {
            throw new InvalidInputException("A value for 'diagnosis' is required when 'medicine' is entered");
        }

        //요청되는 입력 값들이 다 null 값이면 안 됨: 삭제가 아닌 수정이므로
        //“diagnosis”, “weight”, “bloodSugarLevel”, “specialNote” 중 적어도 한가지 필수 입력 확인
        if (diagnosis == null && weight == null && bloodSugarLevel == null && specialNote == null) {
            //요청되는 데일리 기록 입력 값들이 다 null 값이면
            throw new InvalidInputException("At least one of 'diagnosis', 'weight', 'bloodSugarLevel', or 'specialNote' must be provided: You must use delete api");
        }

        //해당 petId와 날짜의 처방전(진단명, 처방약), 몸무게, 혈당, 특이사항 각 DB의 해당 테이블에 값 update
        boolean weightUpdated = false;
        boolean bloodSugarLevelUpdated = false;

        //처방전(진단명, 처방약)
        if (dataPrescriptionRecord.isPresent()) {
            //해당 "petId", "recordDate"로 처방전(진단명, 처방약) 기록 레코드 있는 경우
            if (diagnosis != null) {
                //처방전(진단명, 처방약) 저장
                //prescription_record update 진행
                PrescriptionRecordEntity data = dataPrescriptionRecord.get();
                data.setDiagnosis(diagnosis);
                data.setMedicine(medicine);

                prescriptionRecordRepository.save(data);
            } else {
                //diagnosis == null && medicine == null 인 경우
                PrescriptionRecordEntity data = dataPrescriptionRecord.get();

                prescriptionRecordRepository.delete(data);
            }
        } else {
            //해당 "petId", "recordDate"로 처방전(진단명, 처방약) 기록 레코드 없는 경우
            if (diagnosis != null) {
                //처방전(진단명, 처방약) 저장
                //prescription_record create 진행
                PrescriptionRecordEntity data = new PrescriptionRecordEntity();

                data.setPrescriptionRecordDate(recordDate);
                data.setDiagnosis(diagnosis);
                data.setMedicine(medicine);
                //조회한 PetEntity를 PrescriptionRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);

                prescriptionRecordRepository.save(data);
            }
        }

        //몸무게
        if (dataWeightRecord.isPresent()) {
            //해당 "petId", "recordDate"로 몸무게 기록 레코드 있는 경우
            if (weight != null) {
                //몸무게 저장
                //weight_record update 진행
                WeightRecordEntity data = dataWeightRecord.get();
                data.setWeight(weight);

                weightRecordRepository.save(data);
                weightUpdated = true;
            } else {
                //weight == null 인 경우
                WeightRecordEntity data = dataWeightRecord.get();

                weightRecordRepository.delete(data);
                weightUpdated = true;
            }
        } else {
            //해당 "petId", "recordDate"로 몸무게 기록 레코드 없는 경우
            if (weight != null) {
                //몸무게 저장
                //weight_record create 진행
                WeightRecordEntity data = new WeightRecordEntity();

                data.setWeightRecordDate(recordDate);
                data.setWeight(weight);
                //조회한 PetEntity를 WeightRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);

                weightRecordRepository.save(data);
                weightUpdated = true;
            }
        }

        //혈당
        if (dataBloodSugarLevelRecord.isPresent()) {
            //해당 "petId", "recordDate"로 혈당 기록 레코드 있는 경우
            if (bloodSugarLevel != null) {
                //혈당 저장
                //blood_sugar_level_record update 진행
                BloodSugarLevelRecordEntity data = dataBloodSugarLevelRecord.get();
                data.setBloodSugarLevel(bloodSugarLevel);

                bloodSugarLevelRecordRepository.save(data);
                bloodSugarLevelUpdated = true;
            } else {
                //bloodSugarLevel == null 인 경우
                BloodSugarLevelRecordEntity data = dataBloodSugarLevelRecord.get();

                bloodSugarLevelRecordRepository.delete(data);
                bloodSugarLevelUpdated = true;
            }
        } else {
            //해당 "petId", "recordDate"로 몸무게 기록 레코드 없는 경우
            if (bloodSugarLevel != null) {
                //혈당 저장
                //blood_sugar_level_record create 진행
                BloodSugarLevelRecordEntity data = new BloodSugarLevelRecordEntity();

                data.setSugarRecordDate(recordDate);
                data.setBloodSugarLevel(bloodSugarLevel);
                //조회한 PetEntity를 BloodSugarLevelRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);

                bloodSugarLevelRecordRepository.save(data);
                bloodSugarLevelUpdated = true;
            }
        }

        //특이사항
        if (dataSpecialNoteRecord.isPresent()) {
            //해당 "petId", "recordDate"로 특이사항 기록 레코드 있는 경우
            if (specialNote != null) {
                //특이사항 저장
                //specialNote update 진행
                SpecialNoteRecordEntity data = dataSpecialNoteRecord.get();
                data.setSpecialNote(specialNote);

                specialNoteRecordRepository.save(data);
            } else {
                //specialNote == null 인 경우
                SpecialNoteRecordEntity data = dataSpecialNoteRecord.get();

                specialNoteRecordRepository.delete(data);
            }
        } else {
            //해당 "petId", "recordDate"로 특이사항 기록 레코드 없는 경우
            if (specialNote != null) {
                //특이사항 저장
                //specialNote create 진행
                SpecialNoteRecordEntity data = new SpecialNoteRecordEntity();

                data.setSpecialNoteRecordDate(recordDate);
                data.setSpecialNote(specialNote);
                //조회한 PetEntity를 SpecialNoteRecordEntity의 반려동물 정보로 설정: pet_id
                data.setPet(petEntity);

                specialNoteRecordRepository.save(data);
            }
        }

        //pets 테이블에 current_weight, current_blood_sugar_level 갑 update
        if (weightUpdated) { //weightUpdated: true
            //pets 테이블 current_weight update
            WeightRecordEntity weightRecordEntity = weightRecordRepository.findFirstByPetPetIdOrderByWeightRecordDateDesc(petId);
            if (weightRecordEntity != null) {
                //가장 최신의 weight 값 조회된 경우
                BigDecimal currentWeight = weightRecordEntity.getWeight();
                petEntity.setCurrentWeight(currentWeight);
            } else {
                //weightRecordEntity가 없는 경우 (error발생)
                throw new InvalidInputException("Weight-record must exist at least one");
            }
        }
        if (bloodSugarLevelUpdated) { //bloodSugarLevelUpdated: true
            //pets 테이블 current_blood_sugar_level update
            BloodSugarLevelRecordEntity bloodSugarLevelRecordEntity = bloodSugarLevelRecordRepository.findFirstByPetPetIdOrderBySugarRecordDateDesc(petId);
            if (bloodSugarLevelRecordEntity != null) {
                //가장 최신의 blood_sugar_level 값 조회된 경우
                Short currentBloodSugarLevel = bloodSugarLevelRecordEntity.getBloodSugarLevel();
                petEntity.setCurrentBloodSugarLevel(currentBloodSugarLevel);
            } else {
                //bloodSugarLevelRecordEntity가 없는 경우, currentBloodSugarLevel을 null로 설정
                petEntity.setCurrentBloodSugarLevel(null);
            }
        }
        if (weightUpdated || bloodSugarLevelUpdated) {
            petRepository.save(petEntity);
        }

        return true;
    }

    //daily-record get api
    public DailyRecordDTO getDailyRecord(String petId, LocalDate recordDate) {

        //"petId" 존재 확인
        boolean isExist = petRepository.existsByPetId(petId);
        if (!isExist) { //isExist: false
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 "petId", "recordDate"로 레코드 조회
        Optional<PrescriptionRecordEntity> dataPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> dataWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> dataBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> dataSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        //해당 "petId", "recordDate"로 daily-record 를 등록한 것이 없는지 확인
        if (dataPrescriptionRecord.isEmpty() && dataWeightRecord.isEmpty() && dataBloodSugarLevelRecord.isEmpty() && dataSpecialNoteRecord.isEmpty()) {
            throw new NotFoundException("Daily-Record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //조회한 각 레코드에서 특정 정보 DailyRecordDTO 로 가져오기
        DailyRecordDTO dailyRecordDTO = new DailyRecordDTO();

        dailyRecordDTO.setRecordDate(recordDate);

        if (dataPrescriptionRecord.isPresent()) {
            PrescriptionRecordEntity data = dataPrescriptionRecord.get();

            dailyRecordDTO.setDiagnosis(data.getDiagnosis());
            dailyRecordDTO.setMedicine(data.getMedicine());
        }
        if (dataWeightRecord.isPresent()) {
            WeightRecordEntity data = dataWeightRecord.get();

            dailyRecordDTO.setWeight(data.getWeight());
        }
        if (dataBloodSugarLevelRecord.isPresent()) {
            BloodSugarLevelRecordEntity data = dataBloodSugarLevelRecord.get();

            dailyRecordDTO.setBloodSugarLevel(data.getBloodSugarLevel());
        }
        if (dataSpecialNoteRecord.isPresent()) {
            SpecialNoteRecordEntity data = dataSpecialNoteRecord.get();

            dailyRecordDTO.setSpecialNote(data.getSpecialNote());
        }

        return dailyRecordDTO;
    }

    //daily-record delete api
    @Transactional
    public boolean deleteDailyRecord(String petId, LocalDate recordDate) {

        //PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //해당 "petId", "recordDate"로 레코드 조회
        Optional<PrescriptionRecordEntity> dataPrescriptionRecord = prescriptionRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<WeightRecordEntity> dataWeightRecord = weightRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<BloodSugarLevelRecordEntity> dataBloodSugarLevelRecord = bloodSugarLevelRecordRepository.findByPetIdAndDate(petId, recordDate);
        Optional<SpecialNoteRecordEntity> dataSpecialNoteRecord = specialNoteRecordRepository.findByPetIdAndDate(petId, recordDate);

        //해당 "petId", "recordDate"로 daily-record 를 등록한 것이 없는지 확인
        if (dataPrescriptionRecord.isEmpty() && dataWeightRecord.isEmpty() && dataBloodSugarLevelRecord.isEmpty() && dataSpecialNoteRecord.isEmpty()) {
            //해당 "petId", "recordDate"로 기록된 레코드가 하나도 없는 경우
            throw new NotFoundException("Daily-Record(" + recordDate + ") with PetID " + petId + " not found");
        }

        //해당 petId와 날짜의 처방전(진단명, 처방약), 몸무게, 혈당, 특이사항 각 DB의 해당 테이블 레코드 delete
        boolean weightDeleted = false;
        boolean bloodSugarLevelDeleted = false;

        if (dataPrescriptionRecord.isPresent()) {
            PrescriptionRecordEntity data = dataPrescriptionRecord.get();

            prescriptionRecordRepository.delete(data);
        }
        if (dataWeightRecord.isPresent()) {
            WeightRecordEntity data = dataWeightRecord.get();

            weightRecordRepository.delete(data);
            weightDeleted = true;
        }
        if (dataBloodSugarLevelRecord.isPresent()) {
            BloodSugarLevelRecordEntity data = dataBloodSugarLevelRecord.get();

            bloodSugarLevelRecordRepository.delete(data);
            bloodSugarLevelDeleted = true;
        }
        if (dataSpecialNoteRecord.isPresent()) {
            SpecialNoteRecordEntity data = dataSpecialNoteRecord.get();

            specialNoteRecordRepository.delete(data);
        }

        //pets 테이블에 current_weight, current_blood_sugar_level 갑 update
        if (weightDeleted) { //weightDeleted: true
            //pets 테이블 current_weight update
            WeightRecordEntity weightRecordEntity = weightRecordRepository.findFirstByPetPetIdOrderByWeightRecordDateDesc(petId);
            if (weightRecordEntity != null) {
                //가장 최신의 weight 값 조회된 경우
                BigDecimal currentWeight = weightRecordEntity.getWeight();
                petEntity.setCurrentWeight(currentWeight);
            } else {
                //weightRecordEntity가 없는 경우 (error발생)
                throw new InvalidInputException("Weight-record must exist at least one");
            }
        }
        if (bloodSugarLevelDeleted) { //bloodSugarLevelDeleted: true
            //pets 테이블 current_blood_sugar_level update
            BloodSugarLevelRecordEntity bloodSugarLevelRecordEntity = bloodSugarLevelRecordRepository.findFirstByPetPetIdOrderBySugarRecordDateDesc(petId);
            if (bloodSugarLevelRecordEntity != null) {
                //가장 최신의 blood_sugar_level 값 조회된 경우
                Short currentBloodSugarLevel = bloodSugarLevelRecordEntity.getBloodSugarLevel();
                petEntity.setCurrentBloodSugarLevel(currentBloodSugarLevel);
            } else {
                //bloodSugarLevelRecordEntity가 없는 경우, currentBloodSugarLevel을 null로 설정
                petEntity.setCurrentBloodSugarLevel(null);
            }
        }
        if (weightDeleted || bloodSugarLevelDeleted) {
            petRepository.save(petEntity);
        }

        return true;
    }

    //next hospital visit date update api
    @Transactional
    public boolean updateNextVisitDate(String petId, UpdateNextVisitDateDTO updateNextVisitDateDTO) {

        //UpdateNextVisitDateDTO 에서 값 꺼내야함
        LocalDate nextVisitDate = updateNextVisitDateDTO.getNextVisitDate();
        if (nextVisitDate == null) {
            //next-visit-date에 입력한 값이 null
            throw new InvalidInputException("Next-hospital-visit-date not entered");
        }

        //next-visit-date update 진행: PetEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setNextVisitDate(nextVisitDate);

        petRepository.save(data);

        return true;
    }

    //next hospital visit date delete api
    @Transactional
    public boolean deleteNextVisitDate(String petId) {

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setNextVisitDate(null);

        petRepository.save(data);

        return true;
    }

    //next hospital visit date 조회 api
    public LocalDate getNextVisitDate(String petId) {

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return data.getNextVisitDate();
    }

    //the most recent 7 blood sugar level 조회 api
    public List<BloodSugarLevelRecordDTO> getRecent7BloodSugarLevelRecords(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //상위 7개만을 반환하도록 처리, 기록이 존재하지 않을 경우 빈 리스트 []를 반환
        List<BloodSugarLevelRecordDTO> records = bloodSugarLevelRecordRepository.findByPetIdOrderBySugarRecordDateDesc(petId);
        if (records.size() > 7) {
            return records.subList(0, 7);
        }

        return records;
    }

    //the most recent 7 weight 조회 api
    public List<WeightRecordDTO> getRecent7WeightRecords(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        //상위 7개만을 반환하도록 처리, 기록이 존재하지 않을 경우 빈 리스트 []를 반환
        List<WeightRecordDTO> records = weightRecordRepository.findByPetIdOrderByWeightRecordDateDesc(petId);
        if (records.size() > 7) {
            return records.subList(0, 7);
        }

        return records;
    }

    //target weight update api
    @Transactional
    public boolean updateTargetWeight(String petId,TargetWeightDTO targetWeightDTO) {

        //TargetWeightDTO 에서 값 꺼내야함
        BigDecimal targetWeight = targetWeightDTO.getTargetWeight();
        if (targetWeight == null) {
            //target_weight에 입력한 값이 null
            throw new InvalidInputException("Target_weight not entered");
        }

        //target_weight update 진행: PetEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setTargetWeight(targetWeight);

        petRepository.save(data);

        return true;
    }

    //target weight delete api
    @Transactional
    public boolean deleteTargetWeight(String petId) {

        //petId로 PetEntity 조회
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        data.setTargetWeight(null);

        petRepository.save(data);

        return true;
    }

    //target weight 조회 api
    public BigDecimal getTargetWeight(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        return petEntity.getTargetWeight();
    }

    //insulin-time1,2,3 get api
    public List<LocalTime> getInsulinTime(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        LocalTime insulinTime1 = petEntity.getInsulinTime1();
        LocalTime insulinTime2 = petEntity.getInsulinTime2();
        LocalTime insulinTime3 = petEntity.getInsulinTime3();

        List<LocalTime> insulinTimes = new ArrayList<>();
        insulinTimes.add(insulinTime1);
        insulinTimes.add(insulinTime2);
        insulinTimes.add(insulinTime3);

        return insulinTimes;
    }

    //heartworm-shot,medicine-date get api
    public List<LocalDate> getHeartwormShotMedicineDate(String petId) {

        //petId로 PetEntity 조회
        PetEntity petEntity = petRepository.findByPetId(petId);
        if (petEntity == null){
            //petId에 해당하는 PetEntity가 존재하지 않으면
            throw new NotFoundException("Pet with PetID " + petId + " not found");
        }

        LocalDate heartwormShotDate = petEntity.getHeartwormShotDate();
        LocalDate heartwormMedicineDate = petEntity.getHeartwormMedicineDate();

        List<LocalDate> heartwormShotMedicineDate = new ArrayList<>();
        heartwormShotMedicineDate.add(heartwormShotDate);
        heartwormShotMedicineDate.add(heartwormMedicineDate);

        return heartwormShotMedicineDate;
    }
}
