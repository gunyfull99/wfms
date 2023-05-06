package com.wfms.service;

import com.wfms.entity.WorkFlowStep;

import java.util.List;

public interface WorkFlowStepService {

    WorkFlowStep createWorkFlowStep(WorkFlowStep workFlowStep,Boolean isNew);
    String deleteWorkFlowStep(Long workflowStepId);
    WorkFlowStep updateWorkFlowStep(WorkFlowStep workFlowStep);
    List<WorkFlowStep> listWorkFlowStep(Long workFlowId);
}
