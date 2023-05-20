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
public class TaskUsersDTO {
    private Long taskUserId;
    private Boolean isResponsible;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Long taskId;
    private Integer status;
    private UsersDto userId;
}
