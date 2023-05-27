package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashBoard {
    private Long userId;
    private String username;
    private String fullName;
    private String emailAddress;
    private String address;
    private String phone;
    private String jobTitle;
    private Integer gender;
    private String birthDay;
    private Integer missTime;
    private Integer ontime;
    private Integer numberTaskIsMain;
    private Integer numberTaskIsMember;

}
