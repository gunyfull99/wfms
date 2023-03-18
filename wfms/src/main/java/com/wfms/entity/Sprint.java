package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "sprint")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sprint {
    @Id
    @SequenceGenerator(name = "sprint_generator", sequenceName = "sprint_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sprint_generator")
    @Column(name = "sprint_id")
    private Long sprintId;
    @Column(name = "sprint_name")
    private String sprintName;
    @Column(name = "goal")
    private String goal;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @OneToMany(mappedBy = "sprint")
//    @JsonBackReference
    @JsonIgnore
    private List<Issue> issue;
    @ManyToOne
//    @JsonManagedReference
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Projects projects;

}
