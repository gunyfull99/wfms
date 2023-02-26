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
    @SequenceGenerator(name = "project_users_generator", sequenceName = "project_users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_users_generator")
    @Column(name = "project_users_id")
    private Long projectUsersId;

    @Column(name = "project_id")
    private long projectId;


    @Column(nullable = false,name ="user_id" )
    @NotEmpty
    private long userId;

}
