package com.wfms.service.impl;

import com.wfms.entity.WorkFlowTaskType;
import com.wfms.repository.WorkFlowTaskTypeRepository;
import com.wfms.service.WorkFlowTaskTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 
import java.util.Objects;
@Service
public class WorkFlowTaskTypeServiceImpl implements WorkFlowTaskTypeService {
    @Autowired
    private WorkFlowTaskTypeRepository workFlowTaskTypeRepository;

    @Override
    public WorkFlowTaskType createWorkFlowTaskType(WorkFlowTaskType workFlowTaskType) {
        Assert.isTrue(Objects.nonNull(workFlowTaskType.getTaskTypeId()),"ID TaskType must not be null");
        Assert.isTrue(Objects.nonNull(workFlowTaskType.getWorkFlowId()),"ID WorkFlow must not be null");
        workFlowTaskType.setWorkFlowTaskTypeId(null);
        workFlowTaskType.setStatus(1);
        workFlowTaskType.setCreateDate(LocalDateTime.now());

        return workFlowTaskTypeRepository.save(workFlowTaskType);
    }

    @Override
    public WorkFlowTaskType updateWorkFlowTaskType(WorkFlowTaskType workFlowTaskType) {
        Assert.isTrue(Objects.nonNull(workFlowTaskType.getTaskTypeId()),"ID TaskType must not be null");
        Assert.isTrue(Objects.nonNull(workFlowTaskType.getWorkFlowId()),"ID WorkFlow must not be null");
        WorkFlowTaskType workFlowTaskType1 =  workFlowTaskTypeRepository.getDetailWorkFlowTaskType(workFlowTaskType.getTaskTypeId(), workFlowTaskType.getWorkFlowId());
        if(Objects.nonNull(workFlowTaskType1)){
            Assert.isTrue(Objects.nonNull(workFlowTaskType.getStatus()),"Status WorkFlowTaskType must not be null");
            workFlowTaskType1.setUpdateDate(LocalDateTime.now());
            BeanUtils.copyProperties(workFlowTaskTypeRepository.save(workFlowTaskType1), workFlowTaskType);
        }else{
            BeanUtils.copyProperties(workFlowTaskType, workFlowTaskType1);
            workFlowTaskType1.setWorkFlowTaskTypeId(null);
            workFlowTaskType1.setStatus(1);
            workFlowTaskType1.setCreateDate(LocalDateTime.now());
            BeanUtils.copyProperties(workFlowTaskTypeRepository.save(workFlowTaskType1), workFlowTaskType);
        }
        return workFlowTaskType;
    }
}
