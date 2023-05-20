package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 

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
    private LocalDateTime birthDay;
}
