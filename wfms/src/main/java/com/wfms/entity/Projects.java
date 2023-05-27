package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Projects implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "dead_line")
    private LocalDateTime deadLine;
    @Column(name = "priority_id")
    private Long priorityId;
    @Column(name = "status")
    private Integer status;
//    @ManyToOne
//    @JoinColumn(name = "project_type_id")
//    private ProjectType projectTypes;
   // private Long projectTypeId;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "projects")
//    @JsonBackReference
    @JsonIgnore
    private Set<Sprint> sprints = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "projects")
//    @JsonBackReference
    @JsonIgnore
    private Set<DailyReport> dailyReports = new HashSet<>();
    @OneToMany(mappedBy = "projects", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Document> documents = new HashSet<>();
}
