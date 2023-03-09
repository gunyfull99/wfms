package com.wfms.service.impl;

import com.wfms.Dto.WorkFlowStatusDTO;
import com.wfms.entity.WorkFlowStatus;
import com.wfms.repository.WorkFlowStatusRepository;
import com.wfms.service.WorkFlowStatusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
@Service
public class WorkFlowStatusServiceImpl implements WorkFlowStatusService {

    @Autowired
    private WorkFlowStatusRepository workFlowStatusRepository;

    @Override
    public WorkFlowStatusDTO createWorkFlowStatus(WorkFlowStatusDTO workFlowStatusDTO) {
        Assert.isTrue(Objects.nonNull(workFlowStatusDTO.getName()),"Tên WorkFlowStatus không được để trống");
        WorkFlowStatus workFlowStatus = new WorkFlowStatus();
        BeanUtils.copyProperties(workFlowStatusDTO,workFlowStatus);
        workFlowStatus.setWorkFlowStatusId(null);
        workFlowStatus.setStatus(1);
        BeanUtils.copyProperties(workFlowStatusRepository.save(workFlowStatus),workFlowStatusDTO);
        return workFlowStatusDTO;
    }

    @Override
    public WorkFlowStatusDTO updateWorkFlowStatus(WorkFlowStatusDTO workFlowStatusDTO) {
        Assert.isTrue(Objects.nonNull(workFlowStatusDTO.getName()),"Tên WorkFlowStatus không được để trống");
        Assert.isTrue(Objects.nonNull(workFlowStatusDTO.getWorkFlowStatusId()),"ID WorkFlowStatus không được để trống");
        WorkFlowStatus workFlowStatus =workFlowStatusRepository.getById(workFlowStatusDTO.getWorkFlowStatusId());
        Assert.notNull(workFlowStatus,"Không tìm thấy ID WorkFlowStatus");
        BeanUtils.copyProperties(workFlowStatusDTO,workFlowStatus);
        BeanUtils.copyProperties(workFlowStatusRepository.save(workFlowStatus),workFlowStatusDTO);
        return workFlowStatusDTO;
    }

}
