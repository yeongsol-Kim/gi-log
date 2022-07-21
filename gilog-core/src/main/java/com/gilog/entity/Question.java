package com.gilog.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "question")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "reg_datetime")
    private LocalDateTime regDatetime;

    @Column(name = "mod_datetime")
    private LocalDateTime modDatetime;

    @Column(name = "order_number")
    private Long orderNumber;

}
