package com.wfms.Dto;

import com.wfms.entity.WorkFlowTaskType;
import com.wfms.entity.WorkFlowStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowDTO {
    private Long workFlowId;
    private Long projectId;
    private String workFlowName;
    private String descriptor;
    private Integer status;
    List<WorkFlowStep> workFlowStep;
    List<WorkFlowTaskType> workFlowTaskType;
}
