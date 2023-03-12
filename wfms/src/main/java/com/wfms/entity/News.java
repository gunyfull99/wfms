package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
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
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "content")
    private String content;
    @Column(name = "time_recive")
    private Date timeRecive;
    @Column(name = "title")
    private String title;
    @Column(name = "file")
    private String file;
    @Column(name = "status")
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonManagedReference
    private Issue issue;


}
