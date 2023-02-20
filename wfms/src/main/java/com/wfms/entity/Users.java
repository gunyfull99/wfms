package com.wfms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users implements Serializable {
    @Id
    @SequenceGenerator(name = "users_generator", sequenceName = "users_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_generator")
    private long id;

    @NotEmpty(message = "username must not empty")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username must alpha numberic")
    @Size(min = 5, max = 12, message = "username should between 5-12 characters")
    private String username;

    //    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "password must alpha numberic")
//    @Size(min = 8,max = 16,message = "password should between 8-16 characters")
    private String password;

    private String fullName;
    private String email_address;
    private String address;
    private String phone;
    private int status;
    private Date created_date;
    private Date updated_date;
    private String job_title;
    private int gender;
    @ManyToOne
    //@JsonIgnore
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToMany(fetch = EAGER)
    private Set<Roles> roles = new HashSet<>();
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;

}
