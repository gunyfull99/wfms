package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

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
    private Long projectId;

    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name ="user_id" )
    private Long userId;

}
