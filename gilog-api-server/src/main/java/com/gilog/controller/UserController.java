package com.gilog.controller;

import com.gilog.dto.UserDto;
import com.gilog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/api/userinfo")
    public UserDto getLoginUserData(Authentication authentication) {
        return userService.getMyInfo(authentication.getName());
    }
    
    @GetMapping("/api/admin")
    public String adminTest(@RequestParam String code){

        return "Success";
    }

    @GetMapping("/api/oauth2/code/kakao")
    public String kakaoCallback(@RequestParam String code, Model model){
        String accessToken = userService.getAccessToken(code);
        String key = userService.createKakaoUser(accessToken);
        model.addAttribute("key", key);
        return "callbackPage";

        //return accessToken;
    }

    @ResponseBody
    @GetMapping("/api/oauth2/logout/kakao")
    public String logoutKakao(){

        return "로그아웃되었습니다";
    }

//    // 애플 로그인 호출
//    @RequestMapping(value = "/api/login/apple")
//    public @ResponseBody String getAppleAuthUrl(HttpServletRequest request) throws Exception {
//
//        String reqUrl = appleAuthUrl + "/auth/authorize?client_id=" + client_id + "&redirect_uri=" + redirect_uri
//                + "&response_type=code id_token&response_mode=form_post";
//
//        return reqUrl;
//    }

    @ResponseBody
    @PostMapping("/api/oauth2/code/apple")
    public String appleLogin(@RequestHeader("social-token") String socialToken) {
        return userService.userIdFromApple(socialToken);
    }

    // 유저 정보 등록/수정
    @ResponseBody
    @PostMapping("/api/user/info")
    public Long setUserInfo(@RequestBody UserDto userDto, Authentication authentication){
        userDto.setUsername(authentication.getName());

        return userService.setUserInfo(userDto);
    }

    // 회원탈퇴
    @ResponseBody
    @PostMapping("/api/user/delete")
    public void setUserInfo(Authentication authentication){
        userService.deleteUser(authentication.getName());
    }
}
