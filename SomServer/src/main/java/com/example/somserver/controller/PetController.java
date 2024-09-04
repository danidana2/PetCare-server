package com.example.somserver.controller;

import com.example.somserver.dto.*;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.service.PetService;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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

            ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet profile get successful", petProfileDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<PetProfileDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet profile get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet profile summary get api
    @GetMapping("/{petId}/profile/summary")
    public ResponseEntity<ResponseDTO<PetProfileSummaryDTO>> getPetProfileSummary(@PathVariable String petId) {

        try {
            PetProfileSummaryDTO petProfileSummaryDTO = petService.getPetProfileSummary(petId);

            ResponseDTO<PetProfileSummaryDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet profile summary get successful", petProfileSummaryDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<PetProfileSummaryDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<PetProfileSummaryDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet profile summary get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet update api
    @PatchMapping("/{petId}")
    public ResponseEntity<ResponseDTO<Object>> updatePet(@PathVariable String petId, @RequestBody UpdatePetDTO updatePetDTO) {

        try {
            boolean updateResult = petService.updatePet(petId, updatePetDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet update successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record add api
    @PostMapping("/{petId}/daily-record")
    public ResponseEntity<ResponseDTO<Object>> addDailyRecord(@PathVariable String petId, @RequestBody DailyRecordDTO dailyRecordDTO) {

        try {
            boolean addResult = petService.addDailyRecord(petId, dailyRecordDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record add successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (ConflictException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-record update api
    @PatchMapping("/{petId}/daily-record")
    public ResponseEntity<ResponseDTO<Object>> updateDailyRecord(@PathVariable String petId, @RequestBody DailyRecordDTO dailyRecordDTO) {

        try {
            boolean updateResult = petService.updateDailyRecord(petId, dailyRecordDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record update successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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

            ResponseDTO<DailyRecordDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record get successful", dailyRecordDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<DailyRecordDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
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

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-record delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-record delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //next hospital visit date update api
    @PatchMapping("/{petId}/next-visit-date")
    public ResponseEntity<ResponseDTO<Object>> updateNextVisitDate(@PathVariable String petId, @RequestBody UpdateNextVisitDateDTO updateNextVisitDateDTO) {

        try {
            boolean updateResult = petService.updateNextVisitDate(petId,updateNextVisitDateDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Next-hospital-visit-date update successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Next-hospital-visit-date update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //next hospital visit date delete api
    @PatchMapping("/{petId}/next-visit-date/delete")
    public ResponseEntity<ResponseDTO<Object>> deleteNextVisitDate(@PathVariable String petId) {

        try {
            boolean deleteResult = petService.deleteNextVisitDate(petId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Next-hospital-visit-date delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Next-hospital-visit-date delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //next hospital visit date 조회 api
    @GetMapping("/{petId}/next-visit-date")
    public ResponseEntity<ResponseDTO<LocalDate>> getNextVisitDate(@PathVariable String petId) {

        try {
            LocalDate getResult = petService.getNextVisitDate(petId);

            if (getResult == null) {
                ResponseDTO<LocalDate> response = new ResponseDTO<>(HttpStatus.OK.value(), "No next-hospital-visit-date found for PetID " + petId, null);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<LocalDate> response = new ResponseDTO<>(HttpStatus.OK.value(), "Next-hospital-visit-date get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<LocalDate> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<LocalDate> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Next-hospital-visit-date get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //the most recent 7 blood sugar level 조회 api
    @GetMapping("/{petId}/blood-sugar-level/recent/7")
    public ResponseEntity<ResponseDTO<List<BloodSugarLevelRecordDTO>>> getRecent7BloodSugarLevelRecords(@PathVariable String petId) {

        try {
            List<BloodSugarLevelRecordDTO> recentRecords = petService.getRecent7BloodSugarLevelRecords(petId);

            if (recentRecords.isEmpty()) { //recentRecords: [] 빈 리스트인 경우
                ResponseDTO<List<BloodSugarLevelRecordDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "No blood-sugar-level found for PetID " + petId, recentRecords);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<List<BloodSugarLevelRecordDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "The most recent 7 blood-sugar-level get successful", recentRecords);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<List<BloodSugarLevelRecordDTO>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<List<BloodSugarLevelRecordDTO>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The most recent 7 blood-sugar-level get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //the most recent 7 weight 조회 api
    @GetMapping("/{petId}/weight/recent/7")
    public ResponseEntity<ResponseDTO<List<WeightRecordDTO>>> getRecent7WeightRecords(@PathVariable String petId) {

        try {
            List<WeightRecordDTO> recentRecords = petService.getRecent7WeightRecords(petId);

            if (recentRecords.isEmpty()) { //recentRecords: [] 빈 리스트인 경우
                ResponseDTO<List<WeightRecordDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "No weight found for PetID " + petId, recentRecords);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<List<WeightRecordDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "The most recent 7 weight get successful", recentRecords);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<List<WeightRecordDTO>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<List<WeightRecordDTO>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The most recent 7 weight get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //target weight update api
    @PatchMapping("/{petId}/target-weight")
    public ResponseEntity<ResponseDTO<Object>> updateTargetWeight(@PathVariable String petId, @RequestBody TargetWeightDTO targetWeightDTO) {

        try {
            boolean updateResult = petService.updateTargetWeight(petId, targetWeightDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Target-weight update successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Target-weight update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //target weight delete api
    @PatchMapping("/{petId}/target-weight/delete")
    public ResponseEntity<ResponseDTO<Object>> deleteTargetWeight(@PathVariable String petId) {

        try {
            boolean deleteResult = petService.deleteTargetWeight(petId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Target-weight delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Target-weight delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //target weight 조회 api
    @GetMapping("/{petId}/target-weight")
    public ResponseEntity<ResponseDTO<BigDecimal>> getTargetWeight(@PathVariable String petId) {

        try {
            BigDecimal getResult = petService.getTargetWeight(petId);

            if (getResult == null) {
                ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "No target-weight found for PetID " + petId, null);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "Target-weight get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Target-weight get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
