package com.wfms.service.impl;

import com.wfms.entity.WorkFlowIssueType;
import com.wfms.service.WorkFlowIssueTypeService;
import org.springframework.util.Assert;

import java.util.Objects;

public class WorkFlowIssueTypeServiceImpl implements WorkFlowIssueTypeService {
    @Override
    public WorkFlowIssueType createWorkFlowIssueType(WorkFlowIssueType workFlowIssueType) {
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getIssueTypeId()),"ID IssueType không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getWorkFlowId()),"ID WorkFlow không được để trống");
        workFlowIssueType.setWorkFlowId(null);
        workFlowIssueType.setStatus(1);
        return workFlowIssueType;
    }
}
