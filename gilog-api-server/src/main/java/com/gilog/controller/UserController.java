package com.gilog.controller;

import com.gilog.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/admin")
    public String adminTest(@RequestParam String code){

        return "Success";
    }


    @GetMapping("/api/oauth2/code/kakao")
    public String kakaoCallback(@RequestParam String code){
        System.out.println(code);

        String accessToken = userService.getAccessToken(code);

        return userService.createKakaoUser(accessToken);


        //return accessToken;
    }

    @GetMapping("/kakao2")
    public String kakaoCallback2(@RequestParam String code){
        System.out.println(code);
        return code;
    }
}
