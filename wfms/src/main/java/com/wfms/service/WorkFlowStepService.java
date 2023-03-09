package com.wfms.service;

import com.wfms.Dto.WorkFlowStepDTO;
import com.wfms.entity.WorkFlowStep;

import java.util.List;

public interface WorkFlowStepService {

    WorkFlowStepDTO createWorkFlowStep(WorkFlowStepDTO workFlowStep);
    WorkFlowStepDTO updateWorkFlowStep(WorkFlowStepDTO workFlowStep);
    List<WorkFlowStepDTO> listWorkFlowStep(Long workFlowId);
}
