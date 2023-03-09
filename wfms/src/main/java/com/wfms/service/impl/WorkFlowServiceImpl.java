package com.wfms.service.impl;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.Dto.WorkFlowStatusDTO;
import com.wfms.Dto.WorkFlowStepDTO;
import com.wfms.config.Const;
import com.wfms.entity.WorkFlow;
import com.wfms.entity.WorkFlowIssueType;
import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.WorkFlowRepository;
import com.wfms.service.WorkFlowIssueTypeService;
import com.wfms.service.WorkFlowService;
import com.wfms.service.WorkFlowStatusService;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
public class WorkFlowServiceImpl implements   WorkFlowService {

    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private WorkFlowStatusService workFlowStatusService;
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
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);
        WorkFlowStatusDTO  workFlowStatusDTO= workFlowStatusService.createWorkFlowStatus(new WorkFlowStatusDTO().builder().name("TO DO").build());
        workFlowStepService.createWorkFlowStep(new WorkFlowStepDTO().builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFlowStatusId(workFlowStatusDTO.getWorkFlowStatusId()).workFlowStatusName(workFlowStatusDTO.getName()).step(1).build());
        workFlowIssueTypeService.createWorkFlowIssueType(new WorkFlowIssueType().builder()
                .workFlowId(workFlowDTO.getWorkFlowId())
                .issueTypeId(Const.ISSUE_TYPE_STORY).build());
        return workFlowDTO;
    }
}
