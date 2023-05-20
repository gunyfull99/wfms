package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

@Table(name = "task_users")
@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class TaskUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_users_id")
    private Long taskUserId;
    @Column(name ="is_responsible" )
    private Boolean isResponsible=false;
//    @Column(name ="is_tester_responsible" )
//    private Boolean isTesterResponsible=false;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "user_id")
    private Long userId;
}
