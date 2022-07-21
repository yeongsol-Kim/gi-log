package com.gilog.service;

import com.gilog.dto.QuestionDto;
import com.gilog.entity.Question;
import com.gilog.repository.QuestionRepository;
import com.gilog.repository.UserRepositoryInt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepositoryInt userRepositoryInt;

    // 번호로 질문 추출
    public QuestionDto getQuestion(Long count) {
        QuestionDto questionDto = QuestionDto.builder().build();
        try {
            Question question = questionRepository.findByOrderNumber(count);
            questionDto.setQuestion(question.getQuestion());
        } catch (Exception e) {
            questionDto.setQuestion("직접 질문을 만들어보세요!");
        }

        return questionDto;
    }


}
