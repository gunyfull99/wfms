package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "daily_report")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReport {
    @Id
    @SequenceGenerator(name = "daily_report_generator", sequenceName = "daily_report_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_report_generator")
    @Column(name = "daily_report_id")
    private Long dailyReportId;
    @Column(name = "goal")
    private String goal;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date_estimate")
    private LocalDateTime endDateEstimate;
    @Column(name = "status_work")
    private Integer statusWork;
    @Column(name = "problems_encountered")
    private String problemsEncountered;
    @Column(name = "note")
    private String note;
    @Column(name = "estimate_work")
    private Double estimateWork;
    @Column(name = "member_do_work")
    private String memberDoWork;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Projects projects;
}
