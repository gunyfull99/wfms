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
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
              .workFLowStepName("TO DO").step(1).start(true).build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("IN PROGRESS").step(2).build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("IN TEST").step(3).build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("DONE").step(4).resolve(true).build(),true);
        workFlowIssueTypeService.createWorkFlowIssueType( WorkFlowIssueType.builder()
                .workFlowId(workFlowDTO.getWorkFlowId())
                .issueTypeId(Const.ISSUE_TYPE_STORY).build());
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO updateWorkFlow(WorkFlowDTO workFlowDTO) {
        Assert.isTrue(Objects.nonNull(workFlowDTO.getProjectId()),"ProjectID không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowDTO.getWorkFlowId()),"WorkFlowID không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowDTO.getStatus()),"Status WorkFLow không được để trống");
        WorkFlow workFlow = workFlowRepository.findById(workFlowDTO.getWorkFlowId()).get();
        Assert.notNull((workFlow),"Không tìm thấy workFLowId "+workFlow.getWorkFlowId());
        if(DataUtils.listNotNullOrEmpty(workFlowDTO.getWorkFlowStep())){
            List<WorkFlowStep> listWorkFlowStepStart=workFlowDTO.getWorkFlowStep().stream().filter(WorkFlowStep ::getStart).collect(Collectors.toList());
            Assert.isTrue(DataUtils.listNotNullOrEmpty(listWorkFlowStepStart),"Chưa chọn step start");
            List<WorkFlowStep> listWorkFlowStepResolve=workFlowDTO.getWorkFlowStep().stream().filter(WorkFlowStep ::getResolve).collect(Collectors.toList());
            Assert.isTrue(DataUtils.listNotNullOrEmpty(listWorkFlowStepResolve),"Chưa chọn step resolve");
            List<WorkFlowStep>  listStep= workFlowDTO.getWorkFlowStep();
            for (int i = 0; i <listStep.size() ; i++) {
                workFlowStepService.updateWorkFlowStep(listStep.get(i));
            }
        }
        if(DataUtils.listNotNullOrEmpty(workFlowDTO.getWorkFlowIssueType())){
            List<WorkFlowIssueType>  listWorkFlowIssType= workFlowDTO.getWorkFlowIssueType();
            for (int i = 0; i <listWorkFlowIssType.size() ; i++) {
                workFlowIssueTypeService.updateWorkFlowIssueType(listWorkFlowIssType.get(i));
            }
        }
        BeanUtils.copyProperties(workFlowDTO,workFlow);
        workFlow.setUpdateDate(new Date());
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);
        return workFlowDTO;
    }

    @Override
    public WorkFlow getDetailWorkflow(Long projectId) {
        Assert.isTrue(Objects.nonNull(projectId),"ProjectID không được để trống");
        return workFlowRepository.getDetailWorkflow(projectId);
    }
}
