package com.example.somserver.controller;

import com.example.somserver.dto.JoinDTO;
import com.example.somserver.dto.ResponseDTO;
import com.example.somserver.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@ResponseBody
public class JoinController {

    //JoinService 주입 받기
    private final JoinService joinService;

    public JoinController(JoinService joinService) { //생성자 방식으로

        this.joinService = joinService;
    }

    //join 경로에 대한 post 매핑을 요청할 메소드 작성
    //post 경로로 데이터가 날라올 것이기 때문에
    //회원가입 api
    @PostMapping("/join")
    public ResponseEntity<ResponseDTO<Object>> joinProcess(@RequestBody JoinDTO joinDTO) {

        try {
            boolean joinResult = joinService.joinProcess(joinDTO); //앞단에서 받은 JoinDTO 객체 전달

            if (!joinResult) { //joinResult: false
                ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.CONFLICT.value(), "Join failed", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.OK.value(), "Join successful", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseDTO<Object> response = new ResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Join failed", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
