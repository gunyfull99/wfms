package com.wfms.service.impl;

import com.wfms.config.Const;
import com.wfms.entity.Task;
import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.TaskRepository;
import com.wfms.repository.WorkFlowStepRepository;
import com.wfms.service.WorkFlowTaskTypeService;
import com.wfms.service.WorkFlowStepService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class WorkFlowStepServiceImpl implements WorkFlowStepService {
    @Autowired
    private WorkFlowStepRepository workFlowStepRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private WorkFlowTaskTypeService workFlowTaskTypeService;

    @Override
    public WorkFlowStep createWorkFlowStep(WorkFlowStep workFlowStep,Boolean isNew) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()), Const.responseError.workflowId_null);
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step must not be null");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFLowStepName()),"WorkFlowStep Name must not be null");
        WorkFlowStep w = new WorkFlowStep();
        BeanUtils.copyProperties(workFlowStep,w);
        w.setStatus(1);
        w.setWorkFlowStepId(null);
        w.setCreateDate(LocalDateTime.now());
        if(!isNew){
            w.setStep(listWorkFlowStep(workFlowStep.getWorkFlowId()).size()+1);
            Random numGen = new Random();
            String color = "rgb("+(numGen.nextInt(256) + ", " + numGen.nextInt(256) + ", " + numGen.nextInt(256))+")";
            w.setColor(color);
        }

        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep);
        return workFlowStep;
    }

    @Override
    public String deleteWorkFlowStep(Long workflowStepId) {
        Assert.notNull(workflowStepId," ID WorkFlow Step must not be null");
        WorkFlowStep w = workFlowStepRepository.findById(workflowStepId).get();
        Assert.notNull(w,"Not found workflow step with ID "+workflowStepId);
        Assert.isTrue(!w.getStart(),"Do not delete step start");
        Assert.isTrue(!w.getClosed(),"Do not delete step closed");
        List<Task> taskList=taskRepository.getListTaskByStep(workflowStepId);
        Assert.isTrue(!DataUtils.listNotNullOrEmpty(taskList),"Have "+taskList.size() +" task in this step!");
        w.setStatus(0);
        workFlowStepRepository.save(w);
        return "Delete step successfull!";
    }

    @Override
    public WorkFlowStep updateWorkFlowStep(WorkFlowStep workFlowStep) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),Const.responseError.workflowId_null);
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step must not be null");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStatus()),"Status WorkFlowStep must not be null");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStepId())," ID WorkFlowStep must not be null");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFLowStepName())," Name WorkFlowStep must not be null");
        WorkFlowStep w = workFlowStepRepository.getById(workFlowStep.getWorkFlowStepId());
        Assert.notNull(w," Not found workflow step");
        BeanUtils.copyProperties(workFlowStep,w);
        w.setUpdateDate(LocalDateTime.now());
        w.setColor(w.getColor());
        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep)  ;

        return workFlowStep;
    }

    @Override
    public List<WorkFlowStep> listWorkFlowStep(Long workFlowId) {
        Assert.isTrue(Objects.nonNull(workFlowId),Const.responseError.workflowId_null);
        return workFlowStepRepository.getWorkFLowStepByWorkFlowId(workFlowId);
    }


}
