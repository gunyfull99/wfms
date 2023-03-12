package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedules")
public class Schedules {
    @Id
    @SequenceGenerator(name = "schedules_generator", sequenceName = "schedules_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schedules_generator")
    @Column(name = "schedules_id")
    private Long schedulesId;
    @Column(name = "issue_summary")
    private String issueSummary;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "status")
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonManagedReference
    private Projects projects;
    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonManagedReference
    private Issue issue;

}
