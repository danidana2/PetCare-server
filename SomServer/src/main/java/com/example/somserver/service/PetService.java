package com.example.somserver.service;

import com.example.somserver.dto.PetProfileDTO;
import com.example.somserver.dto.UpdatePetDTO;
import com.example.somserver.entity.PetEntity;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class PetService {

    //PetRepository 주입받기
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {

        this.petRepository = petRepository;
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

        return true;
    }

    //pet delete api
    public boolean deletePet(String petId) {

        //petId로 조회한 PetEntity DB에서 삭제
        PetEntity data = petRepository.findByPetId(petId);
        if (data == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            return false;
        }

        petRepository.delete(data);

        return true;
    }
}
