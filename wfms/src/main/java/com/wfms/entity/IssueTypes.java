package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "issue_types")
public class IssueTypes {
    @Id
    @SequenceGenerator(name = "issue_types_generator", sequenceName = "issue_types_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_types_generator")
    @Column(name = "issue_type_id")
    private Long issueTypeId;

    @Column(name = "issue_type_name")
    private String issueTypeName;

    @Column(name = "status")
    private Integer status;
}
