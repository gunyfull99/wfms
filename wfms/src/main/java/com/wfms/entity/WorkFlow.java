package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @SequenceGenerator(name = "work_flow_generator", sequenceName = "work_flow_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_generator")
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "work_flow_name")
    private String workFlowName;
    @Column(name = "descriptor")
    private String descriptor;
    @Column(name = "work_flow_status_id")
    private Integer workFlowStatusId;

    @OneToMany(mappedBy = "work_flow",cascade = CascadeType.ALL)
    private Set<Issue> issue = new HashSet<>();
    @OneToMany(mappedBy = "work_flow",cascade = CascadeType.ALL)
    private Set<IssueTypes> issueTypes = new HashSet<>();


}
