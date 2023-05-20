package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


 import java.time.LocalDateTime; 

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestTaskDto {
    private Long requestTaskId;
    private UsersDto userId;
    private TaskDTO taskId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer status;
    private String reason;
    private Boolean main;
}
