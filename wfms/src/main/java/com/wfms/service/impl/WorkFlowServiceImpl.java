package com.wfms.service.impl;

import com.wfms.Dto.WorkFlowDTO;
import com.wfms.config.Const;
import com.wfms.entity.WorkFlow;
import com.wfms.entity.WorkFlowTaskType;
import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.WorkFlowRepository;
import com.wfms.service.WorkFlowTaskTypeService;
import com.wfms.service.WorkFlowService;
import com.wfms.service.WorkFlowStepService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 
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
    private WorkFlowTaskTypeService workFlowTaskTypeService;
    @Override
    public WorkFlowDTO createWorkFlow(WorkFlowDTO workFlowDTO) {
        Assert.isTrue(Objects.nonNull(workFlowDTO.getProjectId()),Const.responseError.projectId_null);
        WorkFlow workFlow = new WorkFlow();
        BeanUtils.copyProperties(workFlowDTO,workFlow);
        workFlow.setWorkFlowId(null);
        workFlow.setStatus(1);
        workFlow.setCreateDate(LocalDateTime.now());
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
              .workFLowStepName("TO DO").step(1).start(true).closed(false).resolve(false).color("white").build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("IN PROGRESS").start(false).closed(false).step(2).resolve(false).color("green").build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("DONE").step(3).start(false).closed(false).resolve(true).color("blue").build(),true);
        workFlowStepService.createWorkFlowStep(WorkFlowStep.builder().workFlowId(workFlowDTO.getWorkFlowId())
                .workFLowStepName("CLOSED").step(4).closed(true).start(false).resolve(false).color("red").build(),true);
        workFlowTaskTypeService.createWorkFlowTaskType( WorkFlowTaskType.builder()
                .workFlowId(workFlowDTO.getWorkFlowId())
                .taskTypeId(Const.TASK_TYPE_STORY).build());
        return workFlowDTO;
    }

    @Override
    public WorkFlowDTO updateWorkFlow(WorkFlowDTO workFlowDTO) {
        Assert.isTrue(Objects.nonNull(workFlowDTO.getProjectId()),Const.responseError.projectId_null);
        Assert.isTrue(Objects.nonNull(workFlowDTO.getWorkFlowId()),Const.responseError.workflowId_null);
        Assert.isTrue(Objects.nonNull(workFlowDTO.getStatus()),"Status WorkFLow must not be null");
        WorkFlow workFlow = workFlowRepository.findById(workFlowDTO.getWorkFlowId()).get();
        Assert.notNull((workFlow),Const.responseError.workflow_notFound+workFlow.getWorkFlowId());
        if(DataUtils.listNotNullOrEmpty(workFlowDTO.getWorkFlowStep())){
            List<WorkFlowStep> listWorkFlowStepStart=workFlowDTO.getWorkFlowStep().stream().filter(WorkFlowStep ::getStart).collect(Collectors.toList());
            Assert.isTrue(DataUtils.listNotNullOrEmpty(listWorkFlowStepStart),"Please select step start");
            List<WorkFlowStep> listWorkFlowStepClose=workFlowDTO.getWorkFlowStep().stream().filter(WorkFlowStep ::getClosed).collect(Collectors.toList());
            Assert.isTrue(DataUtils.listNotNullOrEmpty(listWorkFlowStepClose),"Please select step close");
        //    List<WorkFlowStep> listWorkFlowStepResolve=workFlowDTO.getWorkFlowStep().stream().filter(WorkFlowStep ::getResolve).collect(Collectors.toList());
         //   Assert.isTrue(DataUtils.listNotNullOrEmpty(listWorkFlowStepResolve),"Chưa chọn step resolve");
            List<WorkFlowStep>  listStep= workFlowDTO.getWorkFlowStep();
            for (int i = 0; i <listStep.size() ; i++) {
                workFlowStepService.updateWorkFlowStep(listStep.get(i));
            }
        }
        if(DataUtils.listNotNullOrEmpty(workFlowDTO.getWorkFlowTaskType())){
            List<WorkFlowTaskType>  listWorkFlowIssType= workFlowDTO.getWorkFlowTaskType();
            for (int i = 0; i <listWorkFlowIssType.size() ; i++) {
                workFlowTaskTypeService.updateWorkFlowTaskType(listWorkFlowIssType.get(i));
            }
        }
        BeanUtils.copyProperties(workFlowDTO,workFlow);
        workFlow.setUpdateDate(LocalDateTime.now());
        BeanUtils.copyProperties(workFlowRepository.save(workFlow),workFlowDTO);
        return workFlowDTO;
    }

    @Override
    public WorkFlow getDetailWorkflow(Long projectId) {
        Assert.isTrue(Objects.nonNull(projectId),Const.responseError.projectId_null);
        return workFlowRepository.getDetailWorkflow(projectId);
    }
}
