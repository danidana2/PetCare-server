package com.example.somserver.service;

import com.example.somserver.dto.CheckPasswordDTO;
import com.example.somserver.dto.UpdateNicknameDTO;
import com.example.somserver.dto.UpdatePasswordDTO;
import com.example.somserver.dto.UserDTO;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class UserService {

    //UserRepository 주입 받기 //BCryptPasswordEncoder 주입 받기
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //nickname update api
    public boolean updateNickname(String userId, UpdateNicknameDTO updateNicknameDTO) {

        try {
            //UpdateNicknameDTO 에서 nickname 값 꺼내야함
            String nickname = updateNicknameDTO.getNickname();

            //UserEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            UserEntity data = userRepository.findByUserId(userId);
            data.setNickname(nickname);

            userRepository.save(data);

            return true;
        } catch (Exception e) {
            //예외가 발생하면 false 반환
            return false;
        }
    }

    //password update api
    public boolean updatePassword(String userId, UpdatePasswordDTO updatePasswordDTO) {

        try {
            //UpdatePasswordDTO 에서 password 값 꺼내야함
            String password = updatePasswordDTO.getPassword();

            //UserEntity에 dto에서 받은 DATA를 옮겨주기 위해서
            UserEntity data = userRepository.findByUserId(userId);
            data.setPassword(bCryptPasswordEncoder.encode(password));

            userRepository.save(data);

            return true;
        } catch (Exception e) {
            //예외가 발생하면 false 반환
            return false;
        }
    }

    //nickname 조회 api
    public String getNicknameByUserId(String userId){

        try {
            //nickname
            String nickname;

            //userId로 조회한 UserEntity에서 nickname 얻기
            UserEntity data = userRepository.findByUserId(userId);
            nickname = data.getNickname();

            return nickname;
        } catch (Exception e) {
            //예외가 발생하면 false 반환
            return "error";
        }
    }

    //password check api
    public boolean checkPassword(CheckPasswordDTO checkPasswordDTO) {

        try {
            //UserDTO 에서 userId, password 값 꺼내야함
            String userId = checkPasswordDTO.getUserId();
            String password = checkPasswordDTO.getPassword();

            //저장된 password를 저장할 myPassword
            String myPassword;

            //userId로 조회한 UserEntity에서 password 얻기
            UserEntity data = userRepository.findByUserId(userId);
            myPassword = data.getPassword();

            //비밀번호 비교
            boolean isSame;
            isSame = bCryptPasswordEncoder.matches(password, myPassword);

            return isSame;
        } catch (Exception e) {
            //예외가 발생하면 false 반환
            return false;
        }
    }

    //user delete api
    public boolean deleteUser(String userId) {

        try {
            //userId로 조회한 UserEntity DB에서 삭제
            UserEntity data = userRepository.findByUserId(userId);
            userRepository.delete(data);

            return true;
        } catch (Exception e) {
            //예외가 발생하면 false 반환
            return false;
        }
    }
}
