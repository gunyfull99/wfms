package com.wfms.service.impl;

import com.wfms.entity.WorkFlowIssueType;
import com.wfms.repository.WorkFlowIssueTypeRepository;
import com.wfms.service.WorkFlowIssueTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;
@Service
public class WorkFlowIssueTypeServiceImpl implements WorkFlowIssueTypeService {
    @Autowired
    private WorkFlowIssueTypeRepository workFlowIssueTypeRepository;

    @Override
    public WorkFlowIssueType createWorkFlowIssueType(WorkFlowIssueType workFlowIssueType) {
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getIssueTypeId()),"ID IssueType không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getWorkFlowId()),"ID WorkFlow không được để trống");
        workFlowIssueType.setWorkFlowIssueTypeId(null);
        workFlowIssueType.setStatus(1);
        workFlowIssueType.setCreateDate(new Date());

        return workFlowIssueTypeRepository.save(workFlowIssueType);
    }

    @Override
    public WorkFlowIssueType updateWorkFlowIssueType(WorkFlowIssueType workFlowIssueType) {
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getIssueTypeId()),"ID IssueType không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowIssueType.getWorkFlowId()),"ID WorkFlow không được để trống");
        WorkFlowIssueType workFlowIssueType1 =  workFlowIssueTypeRepository.getDetailWorkFlowIssueType(workFlowIssueType.getIssueTypeId(),workFlowIssueType.getWorkFlowId());
        if(Objects.nonNull(workFlowIssueType1)){
            Assert.isTrue(Objects.nonNull(workFlowIssueType.getStatus()),"Status WorkFlowIssueType không được để trống");
            workFlowIssueType1.setUpdateDate(new Date());
            BeanUtils.copyProperties(workFlowIssueTypeRepository.save(workFlowIssueType1),workFlowIssueType);
        }else{
            BeanUtils.copyProperties(workFlowIssueType,workFlowIssueType1);
            workFlowIssueType1.setWorkFlowIssueTypeId(null);
            workFlowIssueType1.setStatus(1);
            workFlowIssueType1.setCreateDate(new Date());
            BeanUtils.copyProperties(workFlowIssueTypeRepository.save(workFlowIssueType1),workFlowIssueType);
        }
        return workFlowIssueType;
    }
}
