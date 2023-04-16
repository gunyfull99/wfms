package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "priority")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "priority_id")
    private Long priorityId;
    @Column(name = "priority_name")
    private String priorityName;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL)
//    @JsonBackReference
    @JsonIgnore
    private Set<Task> tasks =new HashSet<>();
}
