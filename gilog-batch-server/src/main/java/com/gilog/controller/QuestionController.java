package com.gilog.controller;

import com.gilog.dto.QuestionDto;
import com.gilog.entity.Question;
import com.gilog.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


    @GetMapping("/question")
    public String questionManagePage(Model model) {
        model.addAttribute("questions", questionService.getAllList());

        return "question/questionList";
    }

    @PostMapping("/question")
    public String questionAdd(String question, Long id) {
        questionService.saveQuestion(question, id);

        return "redirect:/question";
    }
}
