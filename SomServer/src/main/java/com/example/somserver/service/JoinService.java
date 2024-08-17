package com.example.somserver.service;

import com.example.somserver.dto.JoinDTO;
import com.example.somserver.entity.UserEntity;
import com.example.somserver.exception.ConflictException;
import com.example.somserver.exception.NotFoundException;
import com.example.somserver.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    //UserRepository 주입 받기 //BCryptPasswordEncoder 주입 받기
    private final UserRepository userRepository; //객체 변수 선언
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //UserRepository 주입 받기 //BCryptPasswordEncoder 주입 받기
    public JoinService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) { //생성자 방식으로 초기화 시킬 것임

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //회원가입을 진행하는 메소드
    //리턴 값 boolean : 회원가입 완료/실패
    //앞 단인 controller 단에서 이 메서드를 호출 시킴
    public boolean joinProcess(JoinDTO joinDTO) {

        //JoinDTO 에서 userId, password, nickname 값 꺼내야함
        String userId = joinDTO.getUserId();
        String password = joinDTO.getPassword();
        String nickname = joinDTO.getNickname();

        //중복 userId 확인 -> userRepository 에 메서드 작성 필요
        Boolean isExist = userRepository.existsByUserId(userId);
        if (isExist) {

            //이미 존재하면 이 메서드 강제 종료 하도록 return
            //만약 joinProcess가 boolean 이 리턴값이라면 return false; 이렇게 하고
            //앞단 joinController에서 사용자에게 회원가입이 실패했다 알리는 식으로 만들 수 있음
            //중복 아이디로 회원가입 실패
            throw new ConflictException("User with UserID " + userId + " already exists");
        }

        //회원가입 진행: UserEntity에 dto에서 받은 DATA를 옮겨주기 위해서
        UserEntity data = new UserEntity();

        data.setUserId(userId);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setNickname(nickname);
        data.setRole("ROLE_USER");

        //userRepository 한테 이 엔티티 값을 저장하는 메서드
        userRepository.save(data);

        return true; //회원가입 성공 - true 리턴
    }

}
