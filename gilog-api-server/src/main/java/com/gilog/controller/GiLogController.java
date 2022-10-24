package com.gilog.controller;

import com.gilog.dto.GiLogDto;
import com.gilog.service.GiLogService;
import com.gilog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GiLogController {

    private final GiLogService giLogService;
    private final UserService userService;

    @Value("${file.dir}")
    private String filePath;

    @PostMapping("/api/gi-log/image") // 이미지 업로드
    public String imageTest(MultipartFile image, Long id) {

        String dasePath = filePath; //자신의 로컬 저장소
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

    @GetMapping("/api/gi-log/all") // 기록 목록 조회
    public List<GiLogDto> getMyWriteList(Authentication authentication) {
        return giLogService.getMyGiLogList(userService.getIdByUsername(authentication.getName()));
    }

    @GetMapping("/api/gi-log") // 기록 조회
    public GiLogDto getMyWrite(@RequestBody GiLogDto giLogDto,Authentication authentication) {
        return giLogService.getMyGiLogByDate(userService.getIdByUsername(authentication.getName()), giLogDto.getWriteDate());
    }

    @PostMapping("/api/gi-log") // 기록 저장
    public Long saveWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {

        // 기록 이미지 저장
        giLogDto.setUserId(userService.getIdByUsername(authentication.getName()));

        // 기록 데이터 저장
        return giLogService.saveGiLog(giLogDto);
    }


    @GetMapping("/api/gi-log/image/{imageName}") // 기록 이미지 불러오기
    public ResponseEntity<Resource> getMyWriteImage(@PathVariable String imageName) throws MalformedURLException {
        UrlResource resource = new UrlResource("file:" + filePath + imageName);
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(filePath + imageName);
            // 인풋으로 들어온 파일명 .png / .jpg 에 맞게 헤더 타입 설정
            header.add("Content-Type", Files.probeContentType(filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

    @PostMapping("/api/gi-log/edit") // 기록 수정
    public Long patchWrite(@RequestBody GiLogDto giLogDto, Authentication authentication) {

        giLogDto.setUserId(userService.getIdByUsername(authentication.getName()));

        return giLogService.updateGiLog(giLogDto);
    }
}
