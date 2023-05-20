package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.EventDTO;
import com.wfms.entity.Event;

public interface EventService {
     Event createEvent(EventDTO eventDTO) throws FirebaseMessagingException;
     Event updateEvent(Event event) throws FirebaseMessagingException;
     EventDTO getDetailEvent(Long scheduleId);
     ObjectPaging searchEvent(ObjectPaging objectPaging);
}
