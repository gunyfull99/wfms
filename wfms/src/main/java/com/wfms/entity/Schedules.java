package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedules {
    @Id
    @SequenceGenerator(name = "daily_report_generator", sequenceName = "daily_report_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_report_generator")
    @Column(name = "schedules_id")
    private Long schedulesId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "issue_summary")
    private String issueSummary;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;


    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects projects;
    @ManyToOne
    @JoinColumn(name = "issue_id")
    private Issue issue;

}
