package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

@Entity
@Data
@Table(name = "project_users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_users_id")
    private Long projectUsersId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name ="user_id" )
    private Long userId;
    @Column(name ="status" )
    private Integer status;

}
