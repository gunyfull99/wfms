package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue_types")
public class IssueTypes {
    @Id
    @SequenceGenerator(name = "work_flow_status_generator", sequenceName = "work_flow_status_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_status_generator")
    @Column(name = "issue_type")
    private String issueType;
    @Column(name = "issue_type_name")
    private Long issueTypeName;

    @ManyToOne
    @JoinColumn(name = "work_flow_id")
    private WorkFlow workFlow;
}
