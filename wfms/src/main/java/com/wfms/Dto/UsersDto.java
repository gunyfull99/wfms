package com.wfms.Dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.wfms.entity.Company;
import com.wfms.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {
    private long id;
    private String username;
    private String full_name;
    private String email_address;
    private String address;
    private String phone;
    private int status;
    private Date created_date;
    private Date updated_date;
    private String job_title;
    private int gender;
    private Long company;
    private Set<Roles> roles = new HashSet<>();
    private Date birthDay;
}
