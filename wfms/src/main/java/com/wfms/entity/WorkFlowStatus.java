package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "work_flow_status")
public class WorkFlowStatus {
    @Id
    @SequenceGenerator(name = "work_flow_status_generator", sequenceName = "work_flow_status_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_flow_status_generator")
    @Column(name = "work_flow_status_id")
    private Long workFlowStatusId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "work_flow_status",cascade = CascadeType.ALL)
    private Set<Issue> issueSet = new HashSet<>();
    @Column(name = "status")
    private Integer status;

}
