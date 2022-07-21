package com.gilog.repository;

import com.gilog.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = "select * from question order by rand() limit 1", nativeQuery = true)
    Question getRandomQuestion();


    Question findByOrderNumber(Long orderNumber);

    Optional<Question> findTopByOrderByOrderNumberDesc();

}
