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
@Data
@Table(name = "sprint")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sprint {
    @Id
    @SequenceGenerator(name = "sprint_generator", sequenceName = "sprint_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sprint_generator")
    @Column(name = "sprint_id")
    private Long sprintId;
    @Column(name = "sprint_name")
    private String sprintName;
    @Column(name = "goal")
    private String goal;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "project_id")
    private Long projectId;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private Set<Issue> issueSet = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects projects;

}
