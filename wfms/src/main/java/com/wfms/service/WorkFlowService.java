package com.wfms.service;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.entity.WorkFlow;

public interface WorkFlowService {
    WorkFlowDTO createWorkFlow (WorkFlowDTO workFlowDTO);
    WorkFlowDTO updateWorkFlow (WorkFlowDTO workFlowDTO);
    WorkFlow getDetailWorkflow (Long projectId);
}
