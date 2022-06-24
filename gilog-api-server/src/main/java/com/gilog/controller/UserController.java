package com.gilog.controller;

import com.gilog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
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
    public String kakaoCallback(@RequestParam String code, Model model){
        System.out.println(code);

        String accessToken = userService.getAccessToken(code);
        String key = userService.createKakaoUser(accessToken);
        model.addAttribute("key", key);
        return "callbackPage";


        //return accessToken;
    }

    @GetMapping("/kakao2")
    public String kakaoCallback2(@RequestParam String code){
        System.out.println(code);
        return code;
    }
}
