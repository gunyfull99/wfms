package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartIssue {
    private String sprintName;
    private String workFlowStepName;
    private int numberTask;
}
