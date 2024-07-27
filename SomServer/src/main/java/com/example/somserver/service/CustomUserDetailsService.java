package com.example.somserver.service;

import com.example.somserver.dto.CustomUserDetails;
import com.example.somserver.dto.UserDTO;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    //데이터베이스 연결 진행해야함 -> 데이터베이스에 접근할 리포지터리 연결을 위해 주입 받음
    //생성자 방식으로 UserRepository 주입 받음
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    //데이터 베이스에서 특정 유저를 조회해서 리턴
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        //DB에서 조회
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity != null) {

            //UserEntity를 UserDTO로 수정: UserDTO 생성 및 필드 설정
            UserDTO userData = new UserDTO();

            userData.setUserId(userEntity.getUserId());
            userData.setPassword(userEntity.getPassword());
            userData.setNickname(userEntity.getNickname());
            userData.setRole(userEntity.getRole());

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }

        return null;
    }
}
