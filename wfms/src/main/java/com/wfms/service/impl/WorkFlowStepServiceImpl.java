package com.wfms.service.impl;

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

import java.util.Date;
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
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),"ID WorkFlow không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFLowStepName()),"WorkFlowStatus Name không được để trống");
        WorkFlowStep w = new WorkFlowStep();
        BeanUtils.copyProperties(workFlowStep,w);
        w.setStatus(1);
        w.setWorkFlowStepId(null);
        w.setCreateDate(new Date());
        if(!isNew){
            w.setStep(listWorkFlowStep(workFlowStep.getWorkFlowId()).size()+1);
        }
        Random numGen = new Random();
        String color = "rgb("+(numGen.nextInt(256) + ", " + numGen.nextInt(256) + ", " + numGen.nextInt(256))+")";
        w.setColor(color);
        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep);
        return workFlowStep;
    }

    @Override
    public String deleteWorkFlowStep(Long workflowStepId) {
        Assert.notNull(workflowStepId," ID WorkFlow Step không được để trống");
        WorkFlowStep w = workFlowStepRepository.findById(workflowStepId).get();
        Assert.notNull(w," Không tìm thấy ID WorkFlowStep");
        Assert.isTrue(!w.getStart(),"Không được phép xóa step start");
        Assert.isTrue(!w.getClosed(),"Không được phép xóa step closed");
        List<Task> taskList=taskRepository.getListTaskByStep(workflowStepId);
        Assert.isTrue(!DataUtils.listNotNullOrEmpty(taskList),"Còn "+taskList.size() +" task trong step này!");
        w.setStatus(0);
        workFlowStepRepository.save(w);
        return "Xóa step thành công!";
    }

    @Override
    public WorkFlowStep updateWorkFlowStep(WorkFlowStep workFlowStep) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),"ID WorkFlow không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStatus()),"Status WorkFlowStep không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStepId())," ID WorkFlow Step không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFLowStepName())," WorkFlowStep Name không được để trống");
        WorkFlowStep w = workFlowStepRepository.getById(workFlowStep.getWorkFlowStepId());
        Assert.notNull(w," Không tìm thấy ID WorkFlowStep");
        BeanUtils.copyProperties(workFlowStep,w);
        w.setUpdateDate(new Date());
        w.setColor(w.getColor());
        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep)  ;

        return workFlowStep;
    }

    @Override
    public List<WorkFlowStep> listWorkFlowStep(Long workFlowId) {
        Assert.isTrue(Objects.nonNull(workFlowId),"ID WorkFlow không được để trống");
        return workFlowStepRepository.getWorkFLowStepByWorkFlowId(workFlowId);
    }


}
