package com.example.somserver.service;

import com.example.somserver.dto.*;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.entity.WeightRecordEntity;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.UserRepository;
import com.example.somserver.repository.WeightRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    //UserRepository,PetRepository,WeightRecordRepository 주입 받기 //BCryptPasswordEncoder 주입 받기
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final WeightRecordRepository weightRecordRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, PetRepository petRepository, WeightRecordRepository weightRecordRepository,BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.weightRecordRepository = weightRecordRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //nickname update api
    @Transactional
    public boolean updateNickname(String userId, UpdateNicknameDTO updateNicknameDTO) {

        //UpdateNicknameDTO 에서 nickname 값 꺼내야함
        String nickname = updateNicknameDTO.getNickname();

        //UserEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        UserEntity data = userRepository.findByUserId(userId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        data.setNickname(nickname);

        userRepository.save(data);

        return true;
    }

    //password update api
    @Transactional
    public boolean updatePassword(String userId, UpdatePasswordDTO updatePasswordDTO) {

        //UpdatePasswordDTO 에서 password 값 꺼내야함
        String password = updatePasswordDTO.getPassword();

        //UserEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        UserEntity data = userRepository.findByUserId(userId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        data.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(data);

        return true;
    }

    //nickname 조회 api
    public String getNicknameByUserId(String userId){

        //nickname
        String nickname;

        //userId로 조회한 UserEntity에서 nickname 얻기
        UserEntity data = userRepository.findByUserId(userId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        nickname = data.getNickname();

        return nickname;
    }

    //password check api
    public String checkPassword(CheckPasswordDTO checkPasswordDTO) {

        //UserDTO 에서 userId, password 값 꺼내야함
        String userId = checkPasswordDTO.getUserId();
        String password = checkPasswordDTO.getPassword();

        //저장된 password를 저장할 myPassword
        String myPassword;

        //userId로 조회한 UserEntity에서 password 얻기
        UserEntity data = userRepository.findByUserId(userId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        myPassword = data.getPassword();

        //비밀번호 비교
        boolean isSame;
        isSame = bCryptPasswordEncoder.matches(password, myPassword);

        //비교 결과
        if (isSame) {
            return "same";
        } else {
            return "different";
        }
    }

    //user delete api
    @Transactional
    public boolean deleteUser(String userId) {

        //userId로 조회한 UserEntity DB에서 삭제
        UserEntity data = userRepository.findByUserId(userId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        userRepository.delete(data);

        return true;
    }

    //pet add api
    @Transactional
    public boolean addPet(String userId, AddPetDTO addPetDTO) {

        //AddPetDTO 에서 값 꺼내야함
        String petName = addPetDTO.getPetName();
        String breed = addPetDTO.getBreed();
        Byte age = addPetDTO.getAge();
        BigDecimal currentWeight = addPetDTO.getCurrentWeight();
        Boolean isNeutered = addPetDTO.getIsNeutered();
        Character gender = addPetDTO.getGender();
        Boolean hasDiabetes = addPetDTO.getHasDiabetes();

        LocalTime insulinTime1 = addPetDTO.getInsulinTime1();
        LocalTime insulinTime2 = addPetDTO.getInsulinTime2();
        LocalTime insulinTime3 = addPetDTO.getInsulinTime3();
        LocalDate heartwormShotDate = addPetDTO.getHeartwormShotDate();
        LocalDate heartwormMedicineDate = addPetDTO.getHeartwormMedicineDate();

        //petId 작성
        String petId = userId + petName;

        //중복 petId 확인
        Boolean isExist = petRepository.existsByPetId(petId);
        if (isExist) {
            //중복 petId로 pet create 실패
            throw new ConflictException("Pet with PetID " + petId + " already exists");
        }

        //insulin_time1,2,3 앞에 칸부터 입력했는지 확인
        if ((insulinTime1 == null && (insulinTime2 != null || insulinTime3 != null)) || (insulinTime1 != null && insulinTime2 == null && insulinTime3 != null)) {
            throw new InvalidInputException("Invalid insulin_time inputs");
        }

        //pet create 진행: PetEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        PetEntity data = new PetEntity();

        data.setPetId(petId);
        data.setPetName(petName);
        data.setBreed(breed);
        data.setAge(age);
        data.setCurrentWeight(currentWeight);
        data.setIsNeutered(isNeutered);
        data.setGender(gender);
        data.setHasDiabetes(hasDiabetes);

        if (insulinTime1 != null) data.setInsulinTime1(insulinTime1);
        if (insulinTime2 != null) data.setInsulinTime2(insulinTime2);
        if (insulinTime3 != null) data.setInsulinTime3(insulinTime3);
        if (heartwormShotDate != null) data.setHeartwormShotDate(heartwormShotDate);
        if (heartwormMedicineDate != null) data.setHeartwormMedicineDate(heartwormMedicineDate);

        //조회한 UserEntity를 PetEntity의 사용자 정보로 설정: user_id
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }
        data.setUser(userEntity);

        //petRepository 한테 이 엔티티 값을 저장하는 메서드
        petRepository.save(data);

        /* currentWeight값 weight_records 테이블에 지금 날짜로 weight 기록 추가 */

        //현재 Date
        LocalDate currentDate = LocalDate.now();

        //weight_record create 진행: WeightRecordEntity에 DATA를 옮겨주기 위해서
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

    //petId 조회 api
    public List<String> getPetIdsByUserId(String userId){

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        //userId로 해당하는 모든 petId를 가져오기 - 없으면 [] 빈리스트 반환
        return petRepository.findPetIdsByUserId(userId);
    }
}
