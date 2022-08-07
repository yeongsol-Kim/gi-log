package com.gilog.controller;

import com.gilog.dto.GiLogDto;
import com.gilog.service.GiLogService;
import com.gilog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class GiLogController {

    private final GiLogService giLogService;
    private final UserService userService;

    @Value("${file.dir}")
    private String filePath;

    @PostMapping("/api/gi-log/image") // 이미지 업로드
    public String imageTest(MultipartFile image, Long id) {
        System.out.println(image.getName());
        System.out.println(id);

        String dasePath = filePath; //자신의 로컬 저장소
        System.out.println(dasePath);
        String ext = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));; // 파일 확장자
        String saveFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")); // 저장할 파일 이름 (현재 시간)
        String downloadPath = dasePath + saveFileName + ext;

        try {
            image.transferTo(new File(downloadPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        giLogService.setGiLogImagePath(id, saveFileName + ext);

        return image.getOriginalFilename();
    }

    @GetMapping("/api/gi-log") // 기록 조회
    public GiLogDto getMyWrite(@RequestBody GiLogDto giLogDto,Authentication authentication) {
        System.out.println(giLogDto.getWriteDate());

        return giLogService.getMyGiLogByDate(userService.getIdByUsername(authentication.getName()), giLogDto.getWriteDate());
    }

    @PostMapping("/api/gi-log") // 기록 저장
    public Long saveWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {
        // 기록 이미지 저장

        giLogDto.setUserId(userService.getIdByUsername(authentication.getName()));

        // 기록 데이터 저장
        return giLogService.saveGiLog(giLogDto);
    }

    @PostMapping("/api/gi-log/edit") // 기록 수정
    public Long patchWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {

        giLogDto.setUserId(userService.getIdByUsername(authentication.getName()));

        return giLogService.updateGiLog(giLogDto);
    }
}
