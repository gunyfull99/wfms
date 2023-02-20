package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPaging {
    int total ;
    List<UsersDto>accounts_list;
    int page ;
    int limit ;
    String search;
    String role;
    String userType;


    public UsersPaging(int totalElements, List<UsersDto> usersDtoList){
        this.total=totalElements;
        this.accounts_list= usersDtoList;
    }
}
