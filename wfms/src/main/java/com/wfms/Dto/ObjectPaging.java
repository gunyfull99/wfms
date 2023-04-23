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
public class ObjectPaging {
    Integer total ;
    Object data;
    Integer status;
    Long projectId;
    Long sprintId;
    Long userId;
    Long stepId;
    Boolean createByPm;
    String date;
    String keyword;
    Integer page ;
    Integer limit ;
}
