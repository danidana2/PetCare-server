package com.example.somserver.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.example.somserver.dto.*;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.entity.WalkingPostCommentEntity;
import com.example.somserver.entity.WalkingPostEntity;
import com.example.somserver.exception.ImageDeleteErrorException;
import com.example.somserver.exception.ImageSaveErrorException;
import com.example.somserver.exception.InvalidInputException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.PetRepository;
import com.example.somserver.repository.UserRepository;
import com.example.somserver.repository.WalkingPostCommentRepository;
import com.example.somserver.repository.WalkingPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalkingPostService {

    //UserRepository, PetRepository, WalkingPostRepository, WalkingPostCommentRepository 주입받기
    private final UserRepository userRepository;
    private final WalkingPostRepository walkingPostRepository;
    private final WalkingPostCommentRepository walkingPostCommentRepository;
    private final ImageService imageService;

    @Autowired
    public WalkingPostService(UserRepository userRepository, WalkingPostRepository walkingPostRepository, WalkingPostCommentRepository walkingPostCommentRepository, ImageService imageService) {

        this.userRepository = userRepository;
        this.walkingPostRepository = walkingPostRepository;
        this.walkingPostCommentRepository = walkingPostCommentRepository;
        this.imageService = imageService;
    }

    //walking-post add api
    @Transactional
    public boolean addWalkingPost(String userId, AddWalkingPostDTO addWalkingPostDTO, MultipartFile image) {

        //UpdateDailyWalkingRecordDTO 에서 값 꺼내야함
        Boolean isNicknamePublic = addWalkingPostDTO.getIsNicknamePublic();
        String content = addWalkingPostDTO.getContent();

        if ((content == null || content.isEmpty()) && (image == null || image.isEmpty())) {
            //content 와 이미지 둘중 하나는 꼭 있어야 함
            throw new InvalidInputException("Either content or image must be provided");
        }

        //userId 확인
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        //이미지 저장
        String imageUrl = null;
        // 이미지가 첨부된 경우
        if (image != null && !image.isEmpty()) {
            //이미지 s3에 저장 후, s3에 올린 이미지의 src 주소 가져오기
            try {
                imageUrl = imageService.uploadImage(image);
            } catch (IOException e) {
                //파일 전송 관련 오류 처리
                throw new ImageSaveErrorException("Walking-post image upload failed");
            } catch (AmazonServiceException e) {
                //S3 서버 측 오류 처리
                throw new ImageSaveErrorException("Walking-post image upload failed");
            } catch (AmazonClientException e) {
                //네트워크 문제 등의 클라이언트 오류 처리
                throw new ImageSaveErrorException("Walking-post image upload failed");
            }
        }

        //데이터베이스에 게시물 정보 저장
        WalkingPostEntity data = new WalkingPostEntity();

        //walking_post_id 생성 = user_id+LocalDate+LocalTime
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String walkingPostId = userId + currentDate.toString() + currentTime.toString();

        data.setWalkingPostId(walkingPostId);
        data.setIsNicknamePublic(isNicknamePublic);
        data.setWalkingPostDate(currentDate);
        data.setWalkingPostTime(currentTime);
        data.setContent(content);
        data.setImageUrl(imageUrl); //S3에 저장된 이미지 URL 주소, 이미지가 첨부 되지 않았으면 null
        //data.setLikes(0); //처음 좋아요 수: 0개
        //조회한 UserEntity를 WalkingPostEntity의 유저 정보로 설정: user_id
        data.setUser(userEntity);

        walkingPostRepository.save(data);

        return true;
    }

    //walking-post delete api
    @Transactional
    public boolean deleteWalkingPost(String userId, String walkingPostId) {

        if (!userRepository.existsByUserId(userId)) {
            //해당 userId의 유저 정보가 없는 경우
            throw new NotFoundException("User with UserID " + userId + " not found");
        }
        WalkingPostEntity data = walkingPostRepository.findByWalkingPostId(walkingPostId);
        if (data == null) {
            //해당 walkingPostId의 walking-post 가 없는 경우
            throw new NotFoundException("Walking-post with id " + walkingPostId + " not found");
        }

        //요청한 userId(현재 사용자의 userId)와 요청한 walkingPostId의(해당 게시물의) 작성자 userId가 같은지 확인
        String walkingPostUserId = data.getUser().getUserId();
        if (!walkingPostUserId.equals(userId)) {
            //walkingPostUserId != userId 인 경우
            throw new InvalidInputException("You do not have permission: The current user does not match the post author");
        }

        //s3 에서 해당 이미지 삭제
        String imageUrl = data.getImageUrl();
        try {
            imageService.deleteImage(imageUrl);
        } catch (AmazonServiceException e) {
            //S3 서버 측 오류 처리
            throw new ImageDeleteErrorException("Walking-post image delete failed");
        } catch (AmazonClientException e) {
            //네트워크 문제 등의 클라이언트 오류 처리
            throw new ImageDeleteErrorException("Walking-post image delete failed");
        }

        //데이터베이스에 해당 게시물 정보 삭제
        walkingPostRepository.delete(data);

        return true;
    }

    //walking-post get api
    public List<WalkingPostDTO> getWalkingPosts(int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("walkingPostDate").descending().and(Sort.by("walkingPostTime").descending()));
        List<WalkingPostEntity> posts = walkingPostRepository.findAll(pageable).getContent();

        // Entity를 DTO로 변환
        List<WalkingPostDTO> postDTOs = new ArrayList<>();
        for (WalkingPostEntity post : posts) {
            WalkingPostDTO dto = new WalkingPostDTO();

            dto.setWalkingPostId(post.getWalkingPostId());
            //닉네임 설정
            if (post.getIsNicknamePublic()) {
                dto.setNickname(post.getUser().getNickname()); // 작성자의 닉네임 설정
            } else {
                dto.setNickname("비공개");
            }
            dto.setWalkingPostDate(post.getWalkingPostDate());
            dto.setWalkingPostTime(post.getWalkingPostTime());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());

            postDTOs.add(dto);
        }

        return postDTOs;
    }

    //walking-post-comment add api
    @Transactional
    public boolean addWalkingPostComment(String userId, String walkingPostId, AddWalkingPostCommentDTO addWalkingPostCommentDTO) {

        //AddWalkingPostCommentDTO 에서 값 꺼내야함
        Boolean isNicknamePublic = addWalkingPostCommentDTO.getIsNicknamePublic();
        String comment = addWalkingPostCommentDTO.getComment();

        if (isNicknamePublic == null || comment == null || comment.isEmpty()) {
            //isNicknamePublic, comment 둘 중 입력하지 않은 값이 있는 경우
            throw new InvalidInputException("Both isNicknamePublic and comment must be provided");
        }

        //userId 확인
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null){
            //userId에 해당하는 UserEntity가 존재하지 않으면
            throw new NotFoundException("User with UserID " + userId + " not found");
        }

        //walkingPostId 확인
        WalkingPostEntity walkingPostEntity = walkingPostRepository.findByWalkingPostId(walkingPostId);
        if (walkingPostEntity == null) {
            //해당 walkingPostId의 walking-post 가 없는 경우
            throw new NotFoundException("Walking post with id " + walkingPostId + " not found");
        }

        //데이터베이스에 댓글 정보 저장
        WalkingPostCommentEntity data = new WalkingPostCommentEntity();

        //walking_post_comment_id 생성 = user_id+LocalDate+LocalTime
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        String walkingPostCommentId = userId + currentDate.toString() + currentTime.toString();

        data.setWalkingPostCommentId(walkingPostCommentId);
        data.setIsNicknamePublic(isNicknamePublic);
        data.setWalkingPostCommentDate(currentDate);
        data.setWalkingPostCommentTime(currentTime);
        data.setComment(comment);
        //조회한 UserEntity를 WalkingPostCommentEntity의 유저 정보로 설정: user_id
        data.setUser(userEntity);
        //조회한 WalkingPostEntity를 WalkingPostCommentEntity의 게시물 정보로 설정: walking_post_id
        data.setWalkingPost(walkingPostEntity);

        walkingPostCommentRepository.save(data);

        return true;
    }

    //walking-post-comment delete api
    @Transactional
    public boolean deleteWalkingPostComment(String userId, String walkingPostCommentId) {

        if (!userRepository.existsByUserId(userId)) {
            //해당 userId의 유저 정보가 없는 경우
            throw new NotFoundException("User with UserID " + userId + " not found");
        }
        WalkingPostCommentEntity data = walkingPostCommentRepository.findByWalkingPostCommentId(walkingPostCommentId);
        if (data == null) {
            //해당 walkingPostCommentId walking-post-comment 가 없는 경우
            throw new NotFoundException("Walking post comment with id " + walkingPostCommentId + " not found");
        }

        //요청한 userId(현재 사용자의 userId)와 요청한 walkingPostCommentId의(해당 댓글의) 작성자 userId가 같은지 확인
        String walkingPostCommentUserId = data.getUser().getUserId();
        if (!walkingPostCommentUserId.equals(userId)) {
            //walkingPostUserId != userId 인 경우
            throw new InvalidInputException("You do not have permission: The current user does not match the comment author");
        }

        //데이터베이스에 해당 댓글 정보 삭제
        walkingPostCommentRepository.delete(data);

        return true;
    }

    //walking-post-comment get api
    public List<WalkingPostCommentDTO> getWalkingPostComments(String walkingPostId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("walkingPostCommentDate").descending().and(Sort.by("walkingPostCommentTime").descending()));
        List<WalkingPostCommentEntity> comments = walkingPostCommentRepository.findByWalkingPost_WalkingPostId(walkingPostId, pageable).getContent();

        // Entity를 DTO로 변환
        List<WalkingPostCommentDTO> commentDTOs = new ArrayList<>();
        for (WalkingPostCommentEntity comment : comments) {
            WalkingPostCommentDTO dto = new WalkingPostCommentDTO();

            dto.setWalkingPostCommentId(comment.getWalkingPostCommentId());
            // 닉네임 공개 여부에 따라 닉네임 설정
            if (comment.getIsNicknamePublic()) {
                dto.setNickname(comment.getUser().getNickname()); // 작성자의 닉네임 설정
            } else {
                dto.setNickname("비공개");
            }
            dto.setWalkingPostCommentDate(comment.getWalkingPostCommentDate());
            dto.setWalkingPostCommentTime(comment.getWalkingPostCommentTime());
            dto.setComment(comment.getComment());

            commentDTOs.add(dto);
        }

        return commentDTOs;
    }
}
