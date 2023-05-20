package com.wfms.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
 import java.time.LocalDateTime; 

@Entity
@Table(name = "work_flow_task_type")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkFlowTaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_task_type_id")
    private Long workFlowTaskTypeId;
    @Column(name = "task_type_id")
    private Long taskTypeId;
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "status")
    private Integer status;
}
