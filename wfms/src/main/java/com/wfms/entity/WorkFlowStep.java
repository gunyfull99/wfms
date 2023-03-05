package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "work_flow_step")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkFlowStep {
    @Id
    @SequenceGenerator(name = "work_flow_step_generator", sequenceName = "work_flow_step_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_step_generator")
    @Column(name = "work_flow_step_id")
    private Long workFlowStepId;
    @Column(name = "step")
    private Integer step;

    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "work_flow_status_id")
    private Long workFlowStatusId;
    @Column(name = "status")
    private Integer status;
}
