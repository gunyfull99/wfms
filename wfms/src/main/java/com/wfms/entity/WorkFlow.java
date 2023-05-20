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
@Table(name = "work_flow")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "work_flow_name")
    private String workFlowName;
    @Column(name = "descriptor")
    private String descriptor;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
