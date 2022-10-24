package com.gilog.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "gilog")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "question")
    private String question;

    @Column(name = "request")
    private String request;

    @Column(name = "image")
    private String image;

    @Column(name = "write_date")
    private LocalDate writeDate;

}
