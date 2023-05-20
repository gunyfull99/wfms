package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

@Entity
@Table(name = "comment_task")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_task_id")
    private Long commentTaskId;
    @Column(name = "content")
    private String content;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "file")
    private String file;
    @Column(name = "type")
    private String type;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "status")
    private Integer status;
//    @Column(name = "code")
//    private String code;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "task_id")
    private Task task;

}
