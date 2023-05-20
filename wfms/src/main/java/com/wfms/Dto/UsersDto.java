package com.wfms.Dto;


import com.wfms.entity.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersDto {
    private Long id;
    private String username;
    private String fullName;
    private String emailAddress;
    private String address;
    private String phone;
    private Integer status;
    private String jobTitle;
    private int gender;
    private Long company;
    private LocalDateTime birthDay;
    private Set<Roles> roles = new HashSet<>();
}
