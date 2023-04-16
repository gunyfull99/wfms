package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskUsersDTO {
    private Long taskUserId;
    private Boolean isResponsible;
    private Date createDate;
    private Date updateDate;
    private Long taskId;
    private Integer status;
    private UsersDto userId;
}
