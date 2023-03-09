package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowStepDTO {
    private Long workFlowStepId;
    private Integer step;
    private Long workFlowId;
    private Long workFlowStatusId;
    private Integer status;
    private String workFlowStatusName;
}
