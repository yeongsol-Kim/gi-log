package com.gilog.controller;

import com.gilog.dto.UserDto;
import com.gilog.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/api/userinfo")
    public UserDto getLoginUserData(Authentication authentication) {
        System.out.println(authentication.getName());
        return userService.getMyInfo(authentication.getName());
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

    // 유저 정보 등록/수정
    @ResponseBody
    @PostMapping("/api/user/info")
    public Long setUserInfo(@RequestBody UserDto userDto, Authentication authentication){
        userDto.setUsername(authentication.getName());

        return userService.setUserInfo(userDto);
    }

    @GetMapping("/kakao2")
    public String kakaoCallback2(@RequestParam String code){
        System.out.println(code);
        return code;
    }
}
