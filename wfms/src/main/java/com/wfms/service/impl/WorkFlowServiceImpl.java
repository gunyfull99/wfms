package com.wfms.service.impl;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.config.Const;
import com.wfms.entity.WorkFlow;
import com.wfms.entity.WorkFlowIssueType;
import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.WorkFlowRepository;
import com.wfms.service.WorkFlowIssueTypeService;
import com.wfms.service.WorkFlowService;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WorkFlowServiceImpl implements   WorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private WorkFlowStepService workFlowStepService;
    @Autowired
    private WorkFlowIssueTypeService workFlowIssueTypeService;
    @Override
    public WorkFlowDTO createWorkFlow(WorkFlowDTO workFlowDTO) {
        Assert.isTrue(Objects.nonNull(workFlowDTO.getProjectId()),"ProjectID không được để trống");
        WorkFlow workFlow = new WorkFlow();
        BeanUtils.copyProperties(workFlowDTO,workFlow);
        workFlow.setWorkFlowId(null);
        workFlow.setStatus(1);
        workFlow.setCreateDate(new Date());
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);
        workFlowStepService.createWorkFlowStep(new WorkFlowStep().builder().workFlowId(workFlowDTO.getWorkFlowId())
              .workFLowStepName("TO DO").step(1).build());
        workFlowIssueTypeService.createWorkFlowIssueType(new WorkFlowIssueType().builder()
                .workFlowId(workFlowDTO.getWorkFlowId())
                .issueTypeId(Const.ISSUE_TYPE_STORY).build());
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO updateWorkFlow(WorkFlowDTO workFlowDTO) {
        Assert.isTrue(Objects.nonNull(workFlowDTO.getProjectId()),"ProjectID không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowDTO.getWorkFlowId()),"WorkFlowID không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowDTO.getStatus()),"Status WorkFLow không được để trống");
        WorkFlow workFlow = workFlowRepository.getById(workFlowDTO.getWorkFlowId());
        Assert.isTrue(Objects.nonNull(workFlow),"Không tìm thấy workFLowId "+workFlow.getWorkFlowId());
        BeanUtils.copyProperties(workFlowDTO,workFlow);
        workFlow.setUpdateDate(new Date());
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);

        List<WorkFlowStep>  listStep= workFlowDTO.getWorkFlowStep();
        for (int i = 0; i <listStep.size() ; i++) {
            workFlowStepService.updateWorkFlowStep(listStep.get(i));
        }
        List<WorkFlowIssueType>  listWorkFlowIssType= workFlowDTO.getWorkFlowIssueType();
        for (int i = 0; i <listWorkFlowIssType.size() ; i++) {
            workFlowIssueTypeService.updateWorkFlowIssueType(listWorkFlowIssType.get(i));
        }
        return workFlowDTO;
    }
}
