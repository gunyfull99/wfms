package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class News {
    @Id
    @SequenceGenerator(name = "new_generator", sequenceName = "new_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "new_generator")
    @Column(name = "news_id")
    private Long newsId;
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "content")
    private String content;
    @Column(name = "time_recive")
    private LocalDateTime timeRecive;
    @Column(name = "title")
    private String title;
    @Column(name = "file")
    private String file;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;


}
