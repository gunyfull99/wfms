package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Table(name = "issue_users")
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class IssueUsers {
    @Id
    @SequenceGenerator(name = "issue_users_id_generator", sequenceName = "issue_users_id_status_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "issue_users_id_generator")
    @Column(name = "issue_users_id")
    private Long issueType;

    @Column(nullable = false,name ="is_responsible" )
    @NotEmpty
    private Boolean isResponsible;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;
}
