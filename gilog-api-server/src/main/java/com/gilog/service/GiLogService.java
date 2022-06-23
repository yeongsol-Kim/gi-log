package com.gilog.service;

import com.gilog.dto.GiLogDto;
import com.gilog.entity.GiLog;
import com.gilog.repository.GiLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class GiLogService {

    private final GiLogRepository giLogRepository;

    public GiLogService(GiLogRepository giLogRepository) {
        this.giLogRepository = giLogRepository;
    }

    // 기록 데이터 저장
    public void saveGiLog(GiLogDto giLogDto) {
        GiLog giLog = GiLog.builder()
                .question(giLogDto.getQuestion())
                .request(giLogDto.getRequest())
                .userId(giLogDto.getUserId())
                .writeDate(giLogDto.getWriteDate()).build();
        giLogRepository.save(giLog);
    }

    public GiLogDto getMyGiLogByDate(Long userId, LocalDate date) {
        GiLog giLog = giLogRepository.findOneByUserIdAndWriteDate(userId, date);

        return GiLogDto.builder()
                .question(giLog.getQuestion())
                .request(giLog.getRequest())
                .build();
    }

}
