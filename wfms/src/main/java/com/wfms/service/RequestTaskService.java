package com.wfms.service;

import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.RequestTask;
import com.wfms.entity.Task;

import java.util.List;

public interface RequestTaskService {
    String approveRejectRequest(Long requestTaskId,Integer status);
    ObjectPaging searchRequestTask(ObjectPaging objectPaging);
}
