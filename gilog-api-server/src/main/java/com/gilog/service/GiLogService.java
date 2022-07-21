package com.gilog.service;

import com.gilog.dto.GiLogDto;
import com.gilog.entity.GiLog;
import com.gilog.repository.GiLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class GiLogService {

    private final GiLogRepository giLogRepository;

    public GiLogService(GiLogRepository giLogRepository) {
        this.giLogRepository = giLogRepository;
    }

    // 기록 데이터 저장
    public Long saveGiLog(GiLogDto giLogDto) {
        GiLog giLog = GiLog.builder()
                .question(giLogDto.getQuestion())
                .request(giLogDto.getRequest())
                .userId(giLogDto.getUserId())
                .writeDate(giLogDto.getWriteDate()).build();
        return giLogRepository.save(giLog).getId();
    }

    // 기록 데이터 업데이트
    public Long updateGiLog(GiLogDto giLogDto) {
        GiLog giLog = giLogRepository.findOneByUserIdAndWriteDate(giLogDto.getUserId(), giLogDto.getWriteDate());

        giLog.setQuestion(giLogDto.getQuestion());
        giLog.setRequest(giLogDto.getRequest());
        return giLogRepository.save(giLog).getId();
    }

    public GiLogDto getMyGiLogByDate(Long userId, LocalDate date) {
        GiLog giLog = giLogRepository.findOneByUserIdAndWriteDate(userId, date);

        return GiLogDto.builder()
                .question(giLog.getQuestion())
                .request(giLog.getRequest())
                .build();
    }

    public void setGiLogImagePath(Long id, String saveImageName) {
        GiLog gilog = giLogRepository.findById(id).orElse(null);
        if (gilog != null) {
            gilog.setImage(saveImageName);
            giLogRepository.save(gilog);
        }
    }

}
