package com.example.somserver.controller;

import com.example.somserver.dto.JoinDTO;
import com.example.somserver.dto.UserDTO;
import com.example.somserver.service.JoinService;
import com.example.somserver.service.UserService;
import org.springframework.web.bind.annotation.*;

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
    public String updateNickname(@PathVariable String userId, UserDTO userDTO) {

        boolean updateResult = userService.updateNickname(userId, userDTO);

        if (!updateResult) { //updateResult=false인 경우
            return "fail";
        }
        return "success";
    }

    //password update api
    @PatchMapping("{userId}/password")
    public String updatePassword(@PathVariable String userId, UserDTO userDTO) {

        boolean updateResult = userService.updatePassword(userId, userDTO);

        if (!updateResult) { //updateResult=false인 경우
            return "fail";
        }
        return "success";
    }

    //nickname 조회 api
    @GetMapping("/{userId}/nickname")
    public String getNickname(@PathVariable String userId) {

        String nickname = userService.getNicknameByUserId(userId);

        if (nickname.equals("error")) { //updateResult=false인 경우
            return "fail";
        }
        return nickname;
    }

    //password check api
    @PostMapping("/check/password")
    public String checkPassword(UserDTO userDTO) {

        boolean checkResult = userService.checkPassword(userDTO);

        if (!checkResult) { //checkResult=false인 경우
            return "fail";
        }
        return "success";
    }

    //user delete api
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {

        boolean deleteResult = userService.deleteUser(userId);

        if (!deleteResult) { //deleteResult=false인 경우
            return "fail";
        }
        return "success";
    }
}
