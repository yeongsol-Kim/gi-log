package com.gilog.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String gender;

    private Integer age;

    private LocalDateTime regDatetime;

    private Long gilogCount;
}
