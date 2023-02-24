package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Table(name = "issue_users")
@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class IssueUsers {
//    @Column(nullable = false,name = "user_id")
//    @NotEmpty
//    private long user_id;

    @Column(nullable = false,name ="is_responsible" )
    @NotEmpty
    private Boolean isResponsible;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    @JsonIgnore
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users users;
}
