package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ObjectPaging {
    Integer total ;
    Object data;
    Integer status;
    Long projectId;
    Long taskType;
    Long priority;
    Long level;
    Long sprintId;
    Long userId;
    Long taskId;
    List<Long> listTaskId;
    Long stepId;
    Boolean createByPm;
    String date;
    LocalDateTime toDate;
    LocalDateTime fromDate;
    String keyword;
    Integer page ;
    Integer limit ;
}
