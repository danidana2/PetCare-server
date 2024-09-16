package com.example.somserver.controller;

import com.example.somserver.dto.DailyWalkingRecordDTO;
import com.example.somserver.dto.ResponseDTO;
import com.example.somserver.dto.UpdateCurrentTargetWalkingTimeDTO;
import com.example.somserver.dto.UpdateWalkingScheduleDTO;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.service.WalkingManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/walking-management")
public class WalkingManagementController {

    //WalkingManagementService 주입 받기
    private final WalkingManagementService walkingManagementService;

    public WalkingManagementController(WalkingManagementService walkingManagementService) {

        this.walkingManagementService = walkingManagementService;
    }

    @GetMapping
    public String walkingManagementP() {

        return "walking-management controller";
    }

    //current-target-walking-time update api
    @PatchMapping("/{petId}/current-target-walking-time")
    public ResponseEntity<ResponseDTO<Object>> updateCurrentTargetWalkingTime(@PathVariable String petId, @RequestBody UpdateCurrentTargetWalkingTimeDTO updateCurrentTargetWalkingTimeDTO) {

        try {
            boolean updateResult = walkingManagementService.updateCurrentTargetWalkingTime(petId, updateCurrentTargetWalkingTimeDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Current-target-walking-time update successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Current-target-walking-time update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //current-target-walking-time delete api
    @PatchMapping("/{petId}/current-target-walking-time/delete")
    public ResponseEntity<ResponseDTO<Object>> deleteCurrentTargetWalkingTime(@PathVariable String petId) {

        try {
            boolean deleteResult = walkingManagementService.deleteCurrentTargetWalkingTime(petId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Current-target-walking-time delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Current-target-walking-time delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //current-target-walking-time get api
    @GetMapping("/{petId}/current-target-walking-time")
    public ResponseEntity<ResponseDTO<Short>> getCurrentTargetWalkingTime(@PathVariable String petId) {

        try {
            Short getResult = walkingManagementService.getCurrentTargetWalkingTime(petId);

            if (getResult == null) {
                ResponseDTO<Short> response = new ResponseDTO<>(HttpStatus.OK.value(), "No current-target-walking-time found for PetID " + petId, null);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<Short> response = new ResponseDTO<>(HttpStatus.OK.value(), "Current-target-walking-time get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Short> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Short> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Current-target-walking-time get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-schedule update api
    @PatchMapping("/{petId}/walking-schedule")
    public ResponseEntity<ResponseDTO<Object>> updateWalkingSchedule(@PathVariable String petId, @RequestBody UpdateWalkingScheduleDTO updateWalkingScheduleDTO) {

        try {
            boolean updateResult = walkingManagementService.updateWalkingSchedule(petId, updateWalkingScheduleDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-schedule update successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-schedule update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-schedule delete api
    @PatchMapping("/{petId}/walking-schedule/delete")
    public ResponseEntity<ResponseDTO<Object>> deleteWalkingSchedule(@PathVariable String petId) {

        try {
            boolean deleteResult = walkingManagementService.deleteWalkingSchedule(petId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-schedule delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-schedule delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-schedule get api
    @GetMapping("/{petId}/walking-schedule")
    public ResponseEntity<ResponseDTO<LocalTime>> getWalkingSchedule(@PathVariable String petId) {

        try {
            LocalTime getResult = walkingManagementService.getWalkingSchedule(petId);

            if (getResult == null) {
                ResponseDTO<LocalTime> response = new ResponseDTO<>(HttpStatus.OK.value(), "No walking-schedule found for PetID " + petId, null);
                return ResponseEntity.ok(response);
            }
            ResponseDTO<LocalTime> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-schedule get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<LocalTime> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<LocalTime> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-schedule get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-walking-record add api
    @PostMapping("/{petId}/daily-walking-record")
    public ResponseEntity<ResponseDTO<Object>> addDailyWalkingRecord(@PathVariable String petId, @RequestBody DailyWalkingRecordDTO dailyWalkingRecordDTO) {

        try {
            boolean addResult = walkingManagementService.addDailyWalkingRecord(petId, dailyWalkingRecordDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Daily-walking-record add successful", null);
            return ResponseEntity.ok(response);

        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (ConflictException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Daily-walking-record add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //daily-walking-record update api
    //@PatchMapping("/{petId}/daily-walking-record")

    //daily-walking-record delete api
    //@DeleteMapping("/{petId}/daily-walking-record/{recordDate}")

    //daily-walking-record get api
    //@GetMapping("/{petId}/daily-walking-record/{recordDate}")
}
