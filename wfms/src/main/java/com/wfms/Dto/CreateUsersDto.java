package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wfms.entity.Company;
import com.wfms.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUsersDto {
    private String username;
    private String password;
    private String fullName;
    private String emailAddress;
    private String address;
    private String phone;
    private Integer status;
    private String jobTitle;
    private Integer gender;
    private Long company;
    private Long roles ;
    private Date birthDay;
}
