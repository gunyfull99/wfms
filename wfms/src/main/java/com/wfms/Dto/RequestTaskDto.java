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
public class RequestTaskDto {
    private Long requestTaskId;
    private UsersDto userId;
    private TaskDTO taskId;
    private Date createDate;
    private Date updateDate;
    private Integer status;
}
