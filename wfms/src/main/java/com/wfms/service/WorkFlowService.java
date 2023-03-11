package com.wfms.service;

import com.wfms.Dto.WorkFlowDTO;

public interface WorkFlowService {
    WorkFlowDTO createWorkFlow (WorkFlowDTO workFlowDTO);
    WorkFlowDTO updateWorkFlow (WorkFlowDTO workFlowDTO);
}
