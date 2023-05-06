package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.RequestTask;

public interface RequestTaskService {
    String approveRejectRequest(RequestTask requestTask,Integer status) throws FirebaseMessagingException;
    ObjectPaging searchRequestTask(ObjectPaging objectPaging);
}
