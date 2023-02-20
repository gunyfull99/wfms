package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsersDto {
    private String username;
    private String password;
    private String full_name;
    private String email;
    private String userType;
    private String phone;
    private String address;
    private boolean isActive ;
    private long company;
    private Date birthDay;
    private Date startDay;
    private long role ;
}
