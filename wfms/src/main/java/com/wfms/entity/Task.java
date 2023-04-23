package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "assigness")
    private Long assigness;
    @Column(name = "reporter")
    private Long reporter;
    @Column(name = "code")
    private String code;
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
    @Column(name = "dead_line")
    private Date deadLine;
    @Column(name = "parent")
    private Long parent;
    @Column(name = "status")
    private Integer status;
    @Column(name = "work_flow_step_id")
    private Long workFlowStepId;
    @Column(name = "task_type_id")
    private Long taskTypeId;
    @Column(name = "work_flow_id")
    private Long workFlowId;
    @Column(name = "level_difficult_id")
    private Long levelDifficultId;
    @Column(name = "create_by_pm")
    private Boolean createByPm;
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    //@JsonManagedReference
    @JsonIgnore
    private Sprint sprint;
    @ManyToOne
    @JoinColumn(name = "priority_id")
   // @JsonManagedReference
    @JsonIgnore
    private Priority priority;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    //@JsonBackReference
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<CommentTask> commentTasks = new HashSet<>();


}
