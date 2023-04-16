package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "content")
    private String content;
    @Column(name = "description")
    private String description;
    @Column(name = "time_recive")
    private Date timeRecive;
    @Column(name = "title")
    private String title;
    @Column(name = "file")
    private String file;
    @Column(name = "status")
    private Integer status;
    @JoinColumn(name = "task_id")
    private Long taskId;
    @JoinColumn(name = "user_id")
    private Long userId;
    @JoinColumn(name = "project_id")
    private Long projectId;

}
