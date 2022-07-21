package com.gilog.controller;

import com.gilog.dto.QuestionDto;
import com.gilog.service.QuestionService;
import com.gilog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;


    @GetMapping("/api/question")
    public QuestionDto dailyQuestion(Authentication authentication) {
        Long count = userService.getUserGiLogCountByUsername(authentication.getName());
        return questionService.getQuestion(count);
    }

}
