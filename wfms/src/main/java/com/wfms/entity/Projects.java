package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Projects {
    @Id
    @SequenceGenerator(name = "project_generator", sequenceName = "project_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_generator")
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "project_name")
    private String projectName;
    @Column(name = "short_name")
    private String shortName;
    @Column(name = "lead")
    private Long lead;
    @Column(name = "description")
    private String description;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "dead_line")
    private Date deadLine;
    @Column(name = "priority_id")
    private Long priorityId;
    @Column(name = "status")
    private Integer status;
//    @ManyToOne
//    @JoinColumn(name = "project_type_id")
//    private ProjectType projectTypes;
    private Long projectTypeId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "projects")
    @JsonBackReference
    private Set<Sprint> sprints = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "projects")
    @JsonBackReference
    private Set<DailyReport> dailyReports = new HashSet<>();
}
