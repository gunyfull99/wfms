package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "issue")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Issue {
    @Id
    @SequenceGenerator(name = "issue_generator", sequenceName = "issue_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_generator")
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "assigness")
    private Long assigness;
    @Column(name = "reporter")
    private Long reporter;
    @Column(name = "creator")
    private String creator;
    @Column(name = "summary")
    private String summary;
    @Column(name = "description")
    private String description;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "resolution_date")
    private Date resolutionDate;
    @Column(name = "time_estimate")
    private Double timeEstimate;
    @Column(name = "is_archived")
    private Boolean isArchived;
    @Column(name = "archived_by")
    private String archivedBy;
    @Column(name = "archived_date")
    private Date archivedDate;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "approver")
    private String approver;
    @Column(name = "approve_date")
    private Date approveDate;
    @Column(name = "parent")
    private Long parent;
    @Column(name = "status")
    private Integer status;
    @Column(name = "work_flow_step_id")
    private Long workFlowStepId;
    @Column(name = "issue_type_id")
    private Long issueTypeId;
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    @JsonManagedReference
    private Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "priority_id")
    @JsonManagedReference
    private Priority priority;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<CommentIssue> commentIssues= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<DailyReport> dailyReports= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<Schedules> schedules= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private Set<News> news= new HashSet<>();

}
