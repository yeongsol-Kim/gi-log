package com.gilog.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiLogDto {

    private String question;

    private String request;

    private LocalDate writeDate;

    private Long userId;

    private String image;

}
