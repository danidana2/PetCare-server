package com.example.somserver.controller;

import com.example.somserver.dto.DailyRecordDTO;
import com.example.somserver.dto.PetProfileDTO;
import com.example.somserver.dto.ResponseDTO;
import com.example.somserver.dto.UpdatePetDTO;
import com.example.somserver.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/pets")
public class PetController {

    //PetService 주입 받기
    private final PetService petService;

    public PetController(PetService petService) { //생성자 방식으로

        this.petService = petService;
    }

    @GetMapping
    public String petP() {

        return "pet controller";
    }

    //pet profile get api
    @GetMapping("/{petId}/profile")
    public ResponseEntity<ResponseDTO<PetProfileDTO>> getPetProfile(@PathVariable String petId) {

        try {
            PetProfileDTO petProfileDTO = petService.getPetProfile(petId);

            if (petProfileDTO == null) {
                ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Pet profile get failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet profile get successful", petProfileDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet profile get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet update api
    @PatchMapping("/{petId}")
    public ResponseEntity<ResponseDTO<Object>> updatePet(@PathVariable String petId, @RequestBody UpdatePetDTO updatePetDTO) {

        try {
            boolean updateResult = petService.updatePet(petId, updatePetDTO);

            if (!updateResult) { //updateResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Pet update failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet update successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet delete api
    @DeleteMapping("/{petId}")
    public ResponseEntity<ResponseDTO<Object>> deletePet(@PathVariable String petId) {

        try {
            boolean deleteResult = petService.deletePet(petId);

            if (!deleteResult) { //deleteResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Pet delete failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet delete successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record add api
    @PostMapping("/{petId}/daily-record")
    public ResponseEntity<ResponseDTO<Object>> addDailyRecord(@PathVariable String petId, @RequestBody DailyRecordDTO dailyRecordDTO) {

        try {
            String addResult = petService.addDailyRecord(petId, dailyRecordDTO);

            if (addResult.equals("notFound")) { //addResult: notFound
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Daily-record add failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (addResult.equals("isExist")) { //addResult: isExist
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), "Daily-record add failed", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else if (addResult.equals("notValid")) { //addResult: notValid
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Daily-record add failed", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record add successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record update api
    @PatchMapping("/{petId}/daily-record")
    public ResponseEntity<ResponseDTO<Object>> updateDailyRecord(@PathVariable String petId, @RequestBody DailyRecordDTO dailyRecordDTO) {

        try {
            String updateResult = petService.updateDailyRecord(petId, dailyRecordDTO);

            if (updateResult.equals("notFound")) { //updateResult: notFound
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Daily-record update failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (updateResult.equals("notValid")) { //updateResult: notValid
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), "Daily-record update failed", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record update successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record get api
    @GetMapping("/{petId}/daily-record/{recordDate}")
    public ResponseEntity<ResponseDTO<DailyRecordDTO>> getDailyRecord(@PathVariable String petId, @PathVariable LocalDate recordDate) {

        try {
            DailyRecordDTO dailyRecordDTO = petService.getDailyRecord(petId, recordDate);

            if (dailyRecordDTO == null) {
                ResponseDTO<DailyRecordDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Daily-record get failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<DailyRecordDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record get successful", dailyRecordDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ResponseDTO<DailyRecordDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record delete api
    @DeleteMapping("/{petId}/daily-record/{recordDate}")
    public ResponseEntity<ResponseDTO<Object>> deleteDailyRecord(@PathVariable String petId, @PathVariable LocalDate recordDate) {

        try {
            boolean deleteResult = petService.deleteDailyRecord(petId, recordDate);

            if (!deleteResult) { //deleteResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Daily-record delete failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record delete successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
