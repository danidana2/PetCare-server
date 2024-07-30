package com.example.somserver.controller;

import com.example.somserver.dto.*;
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
    public ResponseEntity<ResponseDTO<Object>> updateNickname(@PathVariable String userId, UpdateNicknameDTO updateNicknameDTO) {

        try {
            boolean updateResult = userService.updateNickname(userId, updateNicknameDTO);

            if (!updateResult) { //updateResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Nickname update failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Nickname update successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Nickname update failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //password update api
    @PatchMapping("{userId}/password")
    public ResponseEntity<ResponseDTO<Object>> updatePassword(@PathVariable String userId, UpdatePasswordDTO updatePasswordDTO) {

        try {
            boolean updateResult = userService.updatePassword(userId, updatePasswordDTO);

            if (!updateResult) { //updateResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Password update failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Password update successful", null);
            return ResponseEntity.ok(response);
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

            if (getResult.equals("notFound")) {
                ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Nickname get failed",null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.OK.value(), "Nickname get successful", getResult);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Nickname get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //password check api
    @PostMapping("/check/password")
    public ResponseEntity<ResponseDTO<String>> checkPassword(CheckPasswordDTO checkPasswordDTO) {

        try {
            String checkResult = userService.checkPassword(checkPasswordDTO);

            if (checkResult.equals("notFound")) {
                ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Password check failed",null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<String> response = new ResponseDTO<>(HttpStatus.OK.value(), "Password check successful", checkResult);
            return ResponseEntity.ok(response);
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

            if (!deleteResult) { //deleteResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "User delete failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "User delete successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "User delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //pet create api
    @PostMapping("/{userId}/pet")
    public ResponseEntity<ResponseDTO<Object>> addPet(@PathVariable String userId, AddPetDTO addPetDTO) {

        try {
            String addResult = userService.addPet(userId, addPetDTO);

            if (addResult.equals("notFound")) {
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "Pet create failed", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            } else if (addResult.equals("isExist")) {
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), "Pet create failed", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "User delete successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Pet create failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //petId 조회 api
    @GetMapping("/{userId}/petId")
    public ResponseEntity<ResponseDTO<List<String>>> getPetId(@PathVariable String userId) {

        try {
            List<String> petIds = userService.getPetIdsByUserId(userId);

            if (petIds.isEmpty()) { //petIds: [] 빈 리스트인 경우
                ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "PetId get failed", petIds);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.OK.value(), "PetId get sucessful", petIds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<List<String>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "PetId get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
