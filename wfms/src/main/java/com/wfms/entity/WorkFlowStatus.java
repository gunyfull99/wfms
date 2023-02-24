package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "work_flow_status")
public class WorkFlowStatus {
    @Id
    @SequenceGenerator(name = "work_flow_status_generator", sequenceName = "work_flow_status_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_status_generator")
    @Column(name = "work_flow_status")
    private Integer workFlowStatus;
//    @Column(name = "work_flow_id")
//    private Long workFlowId;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "work_flow_status",cascade = CascadeType.ALL)
    private Set<Issue> issueSet = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "work_flow", joinColumns = @JoinColumn(name = "work_flow_status_id"), inverseJoinColumns = @JoinColumn(name = "work_flow_status"))
    private Set<WorkFlow> workFlows;
}
