package com.example.somserver.controller;

import com.example.somserver.dto.*;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.service.ObesityManagementService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/obesity-management")
public class ObesityManagementController {

    //ObesityManagementService 주입 받기
    private final ObesityManagementService obesityManagementService;

    public ObesityManagementController(ObesityManagementService obesityManagementService) {

        this.obesityManagementService = obesityManagementService;
    }

    @GetMapping
    public String ObesityManagementP() {

        return "obesity-management controller";
    }

    //current-weight get api
    @GetMapping("/{petId}/current-weight")
    public ResponseEntity<ResponseDTO<BigDecimal>> getCurrentWeight(@PathVariable String petId) {

        try {
            BigDecimal getResult = obesityManagementService.getCurrentWeight(petId);

            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "Current-weight get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Current-weight get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //current-weight update api
    @PatchMapping("/{petId}/current-weight")
    public ResponseEntity<ResponseDTO<Object>> updateCurrentWeight(@PathVariable String petId, @RequestBody CurrentWeightDTO currentWeightDTO) {

        try {
            boolean updateResult = obesityManagementService.updateCurrentWeight(petId, currentWeightDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Current-weight update successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Current-weight update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //standard-weight check api
    @GetMapping("/{petId}/check/standard-weight")
    public ResponseEntity<ResponseDTO<String>> checkStandardWeight(@PathVariable String petId) {

        try {
            String checkResult = obesityManagementService.checkStandardWeight(petId);

            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.OK.value(), "Standard-weight check successful", checkResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Standard-weight check failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //obesity-degree check -> daily-calorie calculate api
    @PostMapping("/{petId}/calculate/daily-calorie")
    public ResponseEntity<ResponseDTO<DailyCalorieResultDTO>> calculateDailyCalorie(@PathVariable String petId, @RequestBody DailyCalorieCheckDTO dailyCalorieCheckDTO) {

        try {
            DailyCalorieResultDTO dailyCalorieResultDTO = obesityManagementService.calculateDailyCalorie(petId, dailyCalorieCheckDTO);

            ResponseDTO<DailyCalorieResultDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Obesity-degree check -> Daily-calorie calculate successful", dailyCalorieResultDTO);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<DailyCalorieResultDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<DailyCalorieResultDTO> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDTO<DailyCalorieResultDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Obesity-degree check -> Daily-calorie calculate failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-calorie get api
    @GetMapping("/{petId}/daily-calorie")
    public ResponseEntity<ResponseDTO<DailyCalorieDTO>> getDailyCalorie(@PathVariable String petId) {

        try {
            DailyCalorieDTO dailyCalorieDTO = obesityManagementService.getDailyCalorie(petId);

            ResponseDTO<DailyCalorieDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-calorie get successful", dailyCalorieDTO);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<DailyCalorieDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<DailyCalorieDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-calorie get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //weight-cal-recommended-calories get api
    @GetMapping("/{petId}/weight-cal-recommended-calories")
    public ResponseEntity<ResponseDTO<BigDecimal>> getWeightCalRecommendedCalories(@PathVariable String petId) {

        try {
            BigDecimal getResult = obesityManagementService.getWeightCalRecommendedCalories(petId);

            if (getResult == null) { //getResult: null
                ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "No weight-cal-recommended-calories found for this PetID " + petId, null);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "Weight-cal-recommended-calories get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Weight-cal-recommended-calories get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
