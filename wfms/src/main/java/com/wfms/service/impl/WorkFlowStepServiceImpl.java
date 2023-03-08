package com.wfms.service.impl;

import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.WorkFlowStepRepository;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

public class WorkFlowStepServiceImpl implements WorkFlowStepService {
    @Autowired
    private WorkFlowStepRepository workFlowStepRepository;

    @Override
    public WorkFlowStep createWorkFlowStep(WorkFlowStep workFlowStep) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),"ID WorkFlow không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStatusId()),"ID WorkFlowStatus không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step không được để trống");
        workFlowStep.setStatus(1);
        workFlowStep.setWorkFlowStepId(null);
        return workFlowStepRepository.save(workFlowStep);
    }
}
