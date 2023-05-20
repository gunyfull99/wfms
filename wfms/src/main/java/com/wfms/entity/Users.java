package com.wfms.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

 import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotEmpty(message = "username must not empty")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "username must alpha numberic")
//    @Size(min = 5, max = 12, message = "username should between 5-12 characters")
    private String username;
    //    @Pattern(regexp = "^[a-zA-Z0-9]+$",message = "password must alpha numberic")
//    @Size(min = 8,max = 16,message = "password should between 8-16 characters")
    private String password;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "email_address")
    private String emailAddress;
    private String address;
    private String phone;
    private Integer status;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @Column(name = "job_title")
    private String jobTitle;
    private Integer gender;
    @Column(name = "file_avatar_id")
    private String fileAvatarId;
    private LocalDateTime birthDay;

    @ManyToMany(fetch = EAGER)
    private Set<Roles> roles = new HashSet<>();



}
