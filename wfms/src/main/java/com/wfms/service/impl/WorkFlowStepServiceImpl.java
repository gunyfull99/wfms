package com.wfms.service.impl;

import com.wfms.Dto.WorkFlowStatusDTO;
import com.wfms.Dto.WorkFlowStepDTO;
import com.wfms.entity.WorkFlowStep;
import com.wfms.repository.WorkFlowStepRepository;
import com.wfms.service.WorkFlowStatusService;
import com.wfms.service.WorkFlowStepService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
@Service
public class WorkFlowStepServiceImpl implements WorkFlowStepService {
    @Autowired
    private WorkFlowStepRepository workFlowStepRepository;
    @Autowired
    private WorkFlowStatusService workFlowStatusService;

    @Override
    public WorkFlowStepDTO createWorkFlowStep(WorkFlowStepDTO workFlowStep) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),"ID WorkFlow không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStatusId()),"ID WorkFlowStatus không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStatusName()),"WorkFlow Status Name không được để trống");
        WorkFlowStatusDTO workFlowStatusDTO= workFlowStatusService.createWorkFlowStatus(new WorkFlowStatusDTO().builder().name(workFlowStep.getWorkFlowStatusName()).build());
        Assert.isTrue(Objects.nonNull(workFlowStatusDTO.getWorkFlowStatusId()),"ID WorkFlow không được để trống");
        WorkFlowStep w = new WorkFlowStep();
        BeanUtils.copyProperties(workFlowStep,w);
        w.setStatus(1);
        w.setWorkFlowStepId(null);
        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep)  ;
        return workFlowStep;
    }

    @Override
    public WorkFlowStepDTO updateWorkFlowStep(WorkFlowStepDTO workFlowStep) {
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowId()),"ID WorkFlow không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStatusId()),"ID WorkFlowStatus không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getStep()),"Step không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStep.getWorkFlowStepId())," ID WorkFlow Step không được để trống");
        WorkFlowStep w = workFlowStepRepository.getById(workFlowStep.getWorkFlowStepId());
        Assert.notNull(w," Không tìm thấy ID WorkFlowStep");
        BeanUtils.copyProperties(workFlowStep,w);
        BeanUtils.copyProperties(workFlowStepRepository.save(w),workFlowStep)  ;
        return workFlowStep;
    }

    @Override
    public List<WorkFlowStepDTO> listWorkFlowStep(Long workFlowId) {
        Assert.isTrue(Objects.nonNull(workFlowId),"ID WorkFlow không được để trống");
        return workFlowStepRepository.getWorkFLowStepByWorkFlowId(workFlowId);
    }


}
