package com.example.somserver.controller;

import com.example.somserver.dto.*;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.service.JoinService;
import com.example.somserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    //UserService 주입 받기
    private final UserService userService;

    public UserController(UserService userService) { //생성자 방식으로

        this.userService = userService;
    }

    @GetMapping
    public String userP() {

        return "user controller";
    }

    //nickname update api
    @PatchMapping("/{userId}/nickname")
    public ResponseEntity<ResponseDTO<Object>> updateNickname(@PathVariable String userId, @RequestBody UpdateNicknameDTO updateNicknameDTO) {

        try {
            boolean updateResult = userService.updateNickname(userId, updateNicknameDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Nickname update successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Nickname update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //password update api
    @PatchMapping("{userId}/password")
    public ResponseEntity<ResponseDTO<Object>> updatePassword(@PathVariable String userId, @RequestBody UpdatePasswordDTO updatePasswordDTO) {

        try {
            boolean updateResult = userService.updatePassword(userId, updatePasswordDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Password update successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Password update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //nickname 조회 api
    @GetMapping("/{userId}/nickname")
    public ResponseEntity<ResponseDTO<String>> getNickname(@PathVariable String userId) {

        try {
            String getResult = userService.getNicknameByUserId(userId);

            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.OK.value(), "Nickname get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Nickname get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //password check api
    @PostMapping("/check/password")
    public ResponseEntity<ResponseDTO<String>> checkPassword(@RequestBody CheckPasswordDTO checkPasswordDTO) {

        try {
            String checkResult = userService.checkPassword(checkPasswordDTO);

            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.OK.value(), "Password check successful", checkResult);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(),null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Password check failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //user delete api
    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDTO<Object>> deleteUser(@PathVariable String userId) {

        try {
            boolean deleteResult = userService.deleteUser(userId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "User delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet add api
    @PostMapping("/{userId}/pet")
    public ResponseEntity<ResponseDTO<Object>> addPet(@PathVariable String userId, @RequestBody AddPetDTO addPetDTO) {

        try {
            boolean addResult = userService.addPet(userId, addPetDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Pet add successful", null);
            return ResponseEntity.ok(response);
        } catch (ConflictException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //petId 조회 api
    @GetMapping("/{userId}/petId")
    public ResponseEntity<ResponseDTO<List<String>>> getPetId(@PathVariable String userId) {

        try {
            List<String> petIds = userService.getPetIdsByUserId(userId);

            if (petIds.isEmpty()) { //petIds: [] 빈 리스트인 경우
                ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "No Pets found for this UserID", petIds);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.OK.value(), "PetID get successful", petIds);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "PetID get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
