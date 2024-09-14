package com.example.somserver.controller;

import com.example.somserver.dto.*;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.service.DiabetesManagementService;
import com.example.somserver.service.ObesityManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/diabetes-management")
public class DiabetesManagementController {

    //DiabetesManagementService 주입 받기
    private final DiabetesManagementService diabetesManagementService;

    public DiabetesManagementController(DiabetesManagementService diabetesManagementService) {

        this.diabetesManagementService = diabetesManagementService;
    }

    @GetMapping
    public String diabetesManagementP() {

        return "diabetes-management controller";
    }

    //diabetes-risk check api
    @PostMapping("/{petId}/check/risk")
    public ResponseEntity<ResponseDTO<DiabetesRiskResultDTO>> checkDiabetesRisk(@PathVariable String petId, @RequestBody DiabetesRiskCheckDTO diabetesRiskCheckDTO) {

        try {
            DiabetesRiskResultDTO diabetesRiskResultDTO = diabetesManagementService.checkDiabetesRisk(petId, diabetesRiskCheckDTO);

            ResponseDTO<DiabetesRiskResultDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Diabetes-risk check successful", diabetesRiskResultDTO);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<DiabetesRiskResultDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<DiabetesRiskResultDTO> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDTO<DiabetesRiskResultDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Diabetes-risk check failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-water-intake get api
    @GetMapping("/{petId}/daily-water-intake")
    public ResponseEntity<ResponseDTO<BigDecimal>> getDailyWaterIntake(@PathVariable String petId) {

        try {
            BigDecimal getResult = diabetesManagementService.getDailyWaterIntake(petId);

            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-water-intake get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-water-intake get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //der-calories calculate api
    @PostMapping("/{petId}/der-calories")
    public ResponseEntity<ResponseDTO<BigDecimal>> calculateDerCalories(@PathVariable String petId, @RequestBody CalculateDerCaloriesDTO calculateDerCaloriesDTO) {

        try {
            BigDecimal calculateResult = diabetesManagementService.calculateDerCalories(petId, calculateDerCaloriesDTO);

            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.OK.value(), "DER-calories calculate successful", calculateResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDTO<BigDecimal> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "DER-calories calculate failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //diabetes-risk get api
    @GetMapping("/{petId}/risk")
    public ResponseEntity<ResponseDTO<DiabetesRiskDTO>> getDiabetesRisk(@PathVariable String petId) {

        try {
            DiabetesRiskDTO diabetesRiskDTO = diabetesManagementService.getDiabetesRisk(petId);

            ResponseDTO<DiabetesRiskDTO> response = new ResponseDTO<>(HttpStatus.OK.value(), "Diabetes-risk get successful", diabetesRiskDTO);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<DiabetesRiskDTO> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<DiabetesRiskDTO> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Diabetes-risk get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
