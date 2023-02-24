package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @ManyToOne
    @JoinColumn(name = "project_type_id")
    private Projects projects;
}
