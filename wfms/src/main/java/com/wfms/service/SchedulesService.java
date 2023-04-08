package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.SchedulesDTO;
import com.wfms.entity.Schedules;

public interface SchedulesService {
     Schedules createSchedules(SchedulesDTO schedulesDTO) throws FirebaseMessagingException;
}
