package com.gilog.service;

import com.gilog.dto.QuestionDto;
import com.gilog.entity.Question;
import com.gilog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    // 질문 전체 목록
    public List<QuestionDto> getAllList() {
        List<Question> questions = questionRepository.findAll();
        List<QuestionDto> questionsDto = new ArrayList<>();
        for (Question question : questions) {
            questionsDto.add(
                    QuestionDto.builder()
                            .id(question.getId())
                            .question(question.getQuestion())
                            .build()
            );
        }
        return questionsDto;
    }


    public Long addQuestion(QuestionDto questionDto) {
        return questionRepository.save(
                Question.builder()
                        .question(questionDto.getQuestion())
                        .build()
        ).getId();

    }
}
