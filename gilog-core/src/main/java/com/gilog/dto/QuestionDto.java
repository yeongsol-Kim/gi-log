package com.gilog.dto;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private Long id;

    private String question;

    private LocalDateTime regDatetime;

    private LocalDateTime modDatetime;

    private Long orderNumber;
}
