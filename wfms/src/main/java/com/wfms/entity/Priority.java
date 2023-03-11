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
    @SequenceGenerator(name = "priority_generator", sequenceName = "priority_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "priority_generator")
    @Column(name = "priority_id")
    private Long priorityId;
    @Column(name = "priority_name")
    private String priorityName;
    @Column(name = "status")
    private Integer status;
    @OneToMany(mappedBy = "priority", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Issue> issues =new HashSet<>();
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
}
