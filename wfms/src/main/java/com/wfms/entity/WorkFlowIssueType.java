package com.wfms.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "work_flow_issue_type")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkFlowIssueType {
    @Id
    @SequenceGenerator(name = "work_flow_iss_generator", sequenceName = "work_flow_iss_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_iss_generator")
    @Column(name = "work_flow_issue_type_id")
    private Long workFlowIssueTypeId;

    @Column(name = "issue_type_id")
    private Long issueTypeId;

    @Column(name = "work_flow_id")
    private Long workFlowId;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
}
