package com.gilog.service;

import com.gilog.dto.QuestionDto;
import com.gilog.entity.Question;
import com.gilog.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 랜덤 질문
    public QuestionDto getRandomQuestion() {
        Question randomQuestion = questionRepository.getRandomQuestion();
        return QuestionDto.builder().question(randomQuestion.getQuestion()).build();
    }


}
