package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "content")
    private String content;
    @Column(name = "description")
    private String description;
    @Column(name = "time_recive")
    private LocalDateTime timeRecive;
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
