package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Data
@Table(name = "project_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private long projectId;


    @Column(nullable = false,name ="user_id" )
    @NotEmpty
    private long userId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Projects projects;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
