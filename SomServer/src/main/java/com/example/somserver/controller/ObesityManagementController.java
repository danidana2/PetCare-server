package com.example.somserver.controller;

import com.example.somserver.dto.CurrentWeightDTO;
import com.example.somserver.dto.ResponseDTO;
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
    @PostMapping("/{petId}/check/standard-weight")
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
}
