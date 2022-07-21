package com.gilog.service;

import com.gilog.dto.QuestionDto;
import com.gilog.entity.Question;
import com.gilog.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
                            .orderNumber(question.getOrderNumber())
                            .modDatetime(question.getModDatetime())
                            .regDatetime(question.getRegDatetime())
                            .build()
            );
        }
        return questionsDto;
    }


    // 질문 저장
    public Long saveQuestion(String que, Long id) {

        Question question = Question.builder().build();

        if (id == null) {
            question.setRegDatetime(LocalDateTime.now());
            Question q = questionRepository.findTopByOrderByOrderNumberDesc().orElse(null);
            if (q == null) {
                question.setOrderNumber(1L);
            } else {
                question.setOrderNumber(q.getOrderNumber() + 1);
            }
        } else {
            question = questionRepository.findById(id).orElseThrow();
        }

        question.setModDatetime(LocalDateTime.now());
        question.setQuestion(que);

        return questionRepository.save(question).getId();

    }
}
