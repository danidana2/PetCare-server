package com.example.somserver.controller;

import com.example.somserver.dto.PetProfileDTO;
import com.example.somserver.dto.ResponseDTO;
import com.example.somserver.dto.UpdatePetDTO;
import com.example.somserver.service.PetService;
import com.example.somserver.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
