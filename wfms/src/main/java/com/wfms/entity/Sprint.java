package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
import java.util.List;

@Entity
@Data
@Table(name = "sprint")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sprint_id")
    private Long sprintId;
    @Column(name = "sprint_name")
    private String sprintName;
    @Column(name = "goal")
    private String goal;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @OneToMany(mappedBy = "sprint")
//    @JsonBackReference
    @JsonIgnore
    private List<Task> task;
    @ManyToOne
//    @JsonManagedReference
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Projects projects;

}
