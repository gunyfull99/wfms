package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private String assigness;
    @Column(name = "reporter")
    private LocalDateTime reporter;
    @Column(name = "creator")
    private String creator;
    @Column(name = "summary")
    private String summary;
    @Column(name = "description")
    private String description;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;
    @Column(name = "time_estimate")
    private Double timeEstimate;
    @Column(name = "is_archived")
    private Boolean isArchived;
    @Column(name = "archived_by")
    private String archivedBy;
    @Column(name = "archived_date")
    private LocalDateTime archivedDate;
    @Column(name = "sprint_id")
    private Long sprintId;
    @Column(name = "approver")
    private String approver;
    @Column(name = "approve_date")
    private LocalDateTime approveDate;
    @Column(name = "parent")
    private String parent;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priorityId;

    @ManyToOne
    @JoinColumn(name = "work_flow_id")
    private WorkFlow workFlow;

    @ManyToOne
    @JoinColumn(name = "work_flow_status_id")
    private WorkFlowStatus workFlowStatus;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<CommentIssue> commentIssues= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<DailyReport> dailyReports= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<IssueUsers> issueUsers= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<Schedules> schedules= new HashSet<>();

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private HashSet<News> news= new HashSet<>();

    @ManyToMany(fetch = EAGER)
    private Set<Users> users = new HashSet<>();

}
