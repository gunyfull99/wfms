package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Long stepId;
    String keyword;
    Integer page ;
    Integer limit ;
}
