package com.gilog.service;

import com.gilog.dto.GiLogDto;
import com.gilog.entity.GiLog;
import com.gilog.repository.GiLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    // 기록 목록 가져오기 (로그인 유저)
    public List<GiLogDto> getMyGiLogList(Long userId) {
        List<GiLog> giLogList = giLogRepository.findByUserId(userId);

        // 리스트 DTO 변환
        List<GiLogDto> giLogDtoList = gilogListToDto(giLogList);

        return giLogDtoList;
    }


    // 기록 날짜로 가져오기 (로그인 유저만 가능)
    public GiLogDto getMyGiLogByDate(Long userId, LocalDate date) {
        GiLog giLog = giLogRepository.findOneByUserIdAndWriteDate(userId, date);

        return GiLogDto.builder()
                .question(giLog.getQuestion())
                .request(giLog.getRequest())
                .build();
    }

    // 이미지 저장
    public void setGiLogImagePath(Long id, String saveImageName) {
        GiLog gilog = giLogRepository.findById(id).orElse(null);
        if (gilog != null) {
            gilog.setImage(saveImageName);
            giLogRepository.save(gilog);
        }
    }



    // List<Gilog> -> List<Dto>
    private List<GiLogDto> gilogListToDto(List<GiLog> giLogList) {
        List<GiLogDto> giLogDtoList = new ArrayList<>();
        giLogList.stream().forEach(g ->
                giLogDtoList.add(GiLogDto.builder()
                        .question(g.getQuestion())
                        .request(g.getRequest())
                        .writeDate(g.getWriteDate())
                        .image(g.getImage())
                        .build())
        );
        return giLogDtoList;
    }

}
