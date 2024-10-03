package com.example.somserver.controller;

import com.example.somserver.dto.*;
import com.example.somserver.exception.*;
import com.example.somserver.service.DiabetesManagementService;
import com.example.somserver.service.WalkingPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/walking-post")
public class WalkingPostController {

    //WalkingPostService 주입 받기
    private final WalkingPostService walkingPostService;

    public WalkingPostController(WalkingPostService walkingPostService) {

        this.walkingPostService = walkingPostService;
    }

    @GetMapping
    public String walkingPostP() {

        return "walking-post controller";
    }

    //walking-post add api
    @PostMapping("/{userId}/post")
    public ResponseEntity<ResponseDTO<Object>> addWalkingPost(@PathVariable String userId, @RequestPart("data") AddWalkingPostDTO addWalkingPostDTO, @RequestPart("image") MultipartFile image) {

        try {
            boolean addResult = walkingPostService.addWalkingPost(userId, addWalkingPostDTO, image);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post add successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (ImageSaveErrorException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-post delete api
    @DeleteMapping("/{userId}/post/{walkingPostId}")
    public ResponseEntity<ResponseDTO<Object>> deleteWalkingPost(@PathVariable String userId, @PathVariable String walkingPostId) {

        try {
            boolean deleteResult = walkingPostService.deleteWalkingPost(userId, walkingPostId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.FORBIDDEN.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (ImageDeleteErrorException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-post get api
    @GetMapping("/post")
    public ResponseEntity<ResponseDTO<List<WalkingPostDTO>>> getWalkingPosts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {

        try {
            List<WalkingPostDTO> walkingPosts = walkingPostService.getWalkingPosts(page, size);

            if (walkingPosts.isEmpty()) {
                //walkingPosts 가 비어 있는 경우 예외 발생
                ResponseDTO<List<WalkingPostDTO>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "There are no more walking-post available", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //성공적으로 데이터를 가져온 경우
            ResponseDTO<List<WalkingPostDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post get successful", walkingPosts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<List<WalkingPostDTO>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-post-comment add api
    @PostMapping("/{userId}/{walkingPostId}/comment")
    public ResponseEntity<ResponseDTO<Object>> addWalkingPostComment(@PathVariable String userId, @PathVariable String walkingPostId,@RequestBody AddWalkingPostCommentDTO addWalkingPostCommentDTO) {

        try {
            boolean addResult = walkingPostService.addWalkingPostComment(userId, walkingPostId, addWalkingPostCommentDTO);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post-comment add successful", null);
            return ResponseEntity.ok(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post-comment add failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-post-comment delete api
    @DeleteMapping("/{userId}/comment/{walkingPostCommentId}")
    public ResponseEntity<ResponseDTO<Object>> deleteWalkingPostComment(@PathVariable String userId, @PathVariable String walkingPostCommentId) {

        try {
            boolean deleteResult = walkingPostService.deleteWalkingPostComment(userId, walkingPostCommentId);

            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post-comment delete successful", null);
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (InvalidInputException e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.FORBIDDEN.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post-comment delete failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    //walking-post-comment get api
    @GetMapping("/{walkingPostId}/comment")
    public ResponseEntity<ResponseDTO<List<WalkingPostCommentDTO>>> getWalkingPostComments(@PathVariable String walkingPostId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        try {
            List<WalkingPostCommentDTO> walkingPostComments = walkingPostService.getWalkingPostComments(walkingPostId, page, size);

            if (walkingPostComments.isEmpty()) {
                //walkingPostComments 가 비어 있는 경우 예외 발생
                ResponseDTO<List<WalkingPostCommentDTO>> response = new ResponseDTO<>(HttpStatus.NOT_FOUND.value(), "There are no more walking-post-comment available", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            //성공적으로 데이터를 가져온 경우
            ResponseDTO<List<WalkingPostCommentDTO>> response = new ResponseDTO<>(HttpStatus.OK.value(), "Walking-post-comment get successful", walkingPostComments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<List<WalkingPostCommentDTO>> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Walking-post-comment get failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
