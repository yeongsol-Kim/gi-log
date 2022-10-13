package com.gilog.service;

import com.gilog.entity.GiLog;
import com.gilog.repository.GiLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GiLogServiceTest {

    @Autowired
    private GiLogRepository giLogRepository;
    private TestEntityManager testEntityManager;


    @Test
    void getMyGiLogList() {

        GiLog giLog1 = new GiLog();
        giLog1.setQuestion("question1?");
        giLog1.setRequest("request1");
        giLog1.setUserId(1L);
        giLog1.setWriteDate(LocalDate.parse("2022-10-01"));

        GiLog giLog2 = new GiLog();
        giLog2.setQuestion("question2?");
        giLog2.setRequest("request2");
        giLog2.setUserId(1L);
        giLog2.setWriteDate(LocalDate.parse("2022-10-02"));

        GiLog giLog3 = new GiLog();
        giLog3.setQuestion("question3?");
        giLog3.setRequest("request3");
        giLog3.setUserId(2L);
        giLog3.setWriteDate(LocalDate.parse("2022-10-01"));

        giLogRepository.save(giLog1);
        giLogRepository.save(giLog2);
        giLogRepository.save(giLog3);

        List<GiLog> giLogList = giLogRepository.findByUserId(1L);

        // 리스트 DTO 변환
//        List<GiLogDto> giLogDtoList = gilogListToDto(giLogList);

        Assertions.assertThat(giLogList).contains(giLog1);
        Assertions.assertThat(giLogList).contains(giLog2);
        Assertions.assertThat(giLogList).doesNotContain(giLog3);


        Assertions.assertThat(1).isEqualTo(1);
    }
}