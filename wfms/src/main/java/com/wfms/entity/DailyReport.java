package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "daily_report")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_report_id")
    private Long dailyReportId;
    @Column(name = "goal")
    private String goal;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date_estimate")
    private Date endDateEstimate;
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
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @ManyToOne
    @JoinColumn(name = "task_id")
//    @JsonManagedReference
    @JsonIgnore
    private Task task;

    @ManyToOne
    @JoinColumn(name = "project_id")
//    @JsonManagedReference
    @JsonIgnore
    private Projects projects;
}
