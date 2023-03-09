package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Table(name = "issue_users")
@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class IssueUsers {
    @Id
    @SequenceGenerator(name = "issue_users_id_generator", sequenceName = "issue_users_id_status_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_users_id_generator")
    @Column(name = "issue_users_id")
    private Long issueUserId;
    @Column(nullable = false,name ="is_responsible" )
    private Boolean isResponsible;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "user_id")
    private Long userId;
}
