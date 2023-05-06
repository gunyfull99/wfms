package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "request_task")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_task_id")
    private Long requestTaskId;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "reason")
    private String reason;


}
