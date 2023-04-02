package com.wfms.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserValidateDto {
    private String username;
    private String fullName;
    private String emailAddress;
    private String address;
    private String phone;
    private String gender;
    private int genderCode;
    private String birthDay;
    private String role;
    private Long roleId;
    private String jobTitle;
    private String messageValidate;
}
