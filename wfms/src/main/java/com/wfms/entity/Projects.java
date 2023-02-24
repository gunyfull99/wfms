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
    @Column(name = "key")
    private String key;
    @Column(name = "project_type_id")
    private LocalDateTime projectTypeId;
    @Column(name = "lead")
    private String lead;
    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "projects",cascade = CascadeType.ALL)
    private Set<ProjectType> projectTypeSet = new HashSet<>();

    @OneToMany(mappedBy = "projects",cascade = CascadeType.ALL)
    private Set<Sprint> sprints = new HashSet<>();

    @OneToMany(mappedBy = "projects",cascade = CascadeType.ALL)
    private Set<DailyReport> dailyReports = new HashSet<>();
}
