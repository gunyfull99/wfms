package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project_type")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectType {
    @Id
    @SequenceGenerator(name = "project_type_generator", sequenceName = "project_type_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_type_generator")
    @Column(name = "project_type_id")
    private Long projectTypeId;
    @Column(name = "project_type_name")
    private String projectTypeName;
    @Column(name = "status")
    private Integer status;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;

}
