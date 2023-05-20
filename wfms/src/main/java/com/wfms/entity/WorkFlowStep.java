package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 

@Entity
@Table(name = "work_flow_step")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkFlowStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_step_id")
    private Long workFlowStepId;
    @Column(name = "step")
    private Integer step;
    @Column(name = "start")
    private Boolean start=false;
    @Column(name = "resolve")
    private Boolean resolve=false;
//    @Column(name = "tested")
//    private Boolean tested=false;
    @Column(name = "closed")
    private Boolean closed=false;
    @Column(name = "work_flow_step_name")
    private String workFLowStepName;
    @Column(name = "color")
    private String color;
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
