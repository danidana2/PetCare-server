package com.example.somserver.controller;

import com.example.somserver.dto.JoinDTO;
import com.example.somserver.service.JoinService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ResponseBody
public class JoinController {

    //회원가입 성공 여부 담을 변수
    private boolean joinResult;

    //JoinService 주입 받기
    private final JoinService joinService;

    public JoinController(JoinService joinService) { //생성자 방식으로

        this.joinService = joinService;
    }

    //join 경로에 대한 post 매핑을 요청할 메소드 작성
    //post 경로로 데이터가 날라올 것이기 때문에 post 방식을 받을
    @PostMapping("/join")
    public String joinProcess(JoinDTO joinDTO) {

        joinResult = joinService.joinProcess(joinDTO); //앞단에서 받은 JoinDTO 객체 전달

        if (!joinResult) { //joinResult=false인경우

            return "fail";
        }

        return "success";
    }
}
