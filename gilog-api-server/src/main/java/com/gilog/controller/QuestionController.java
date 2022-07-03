package com.gilog.controller;

import com.gilog.dto.QuestionDto;
import com.gilog.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/api/question")
    public QuestionDto dailyQuestion() {
        return questionService.getRandomQuestion();
    }

}
