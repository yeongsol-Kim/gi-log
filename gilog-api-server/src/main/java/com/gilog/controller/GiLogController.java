package com.gilog.controller;

import com.gilog.dto.GiLogDto;
import com.gilog.service.GiLogService;
import com.gilog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class GiLogController {

    private final GiLogService giLogService;
    private final UserService userService;

    @GetMapping("/api/gi-log") // 기록 조회
    public GiLogDto getMyWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {
        System.out.println(giLogDto.getWriteDate());

        return giLogService.getMyGiLogByDate(userService.getIdByUsername(authentication.getName()), giLogDto.getWriteDate());
    }

    @PostMapping("/api/gi-log") // 기록 저장
    public void saveWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {
        // 기록 이미지 저장


        giLogDto.setUserId(userService.getIdByUsername(authentication.getName()));

        // 기록 데이터 저장
        giLogService.saveGiLog(giLogDto);
    }
}
