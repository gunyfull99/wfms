package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.entity.*;
import com.wfms.repository.NotificationRepository;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.repository.EventRepository;
import com.wfms.service.FireBaseService;
import com.wfms.service.ProjectService;
import com.wfms.service.EventService;
import com.wfms.utils.Constants;
import com.wfms.utils.DataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public Event createEvent(EventDTO eventDTO) throws FirebaseMessagingException {
        Assert.notNull(eventDTO.getMeetingTitle(),"MeetingTitle must not be null");
        Assert.notNull(eventDTO.getStartDate(),"StartDate must not be null");
        Assert.notNull(eventDTO.getMeetingType(),"MeetingType must not be null");
        Assert.notNull(eventDTO.getProjectDTO(),"Project must not be null");
        if(Objects.equals(Constants.OFFLINE_ROOM, eventDTO.getMeetingType())){
             Assert.notNull(eventDTO.getEndDate(),"EndDate must not be null");
            Event event = eventRepository.findEventWithStartTime(eventDTO.getStartDate());
            Assert.notNull(event,"This meeting room already has a group, please choose another meeting room");
            Assert.notNull(eventDTO.getRoomMeeting(),"RoomMeeting must not be null");
        }else if (Objects.equals(Constants.ONLINE_ROOM, eventDTO.getMeetingType())){
            Assert.notNull(eventDTO.getLinkMeeting(),"Link meeting must not be null");
        }
        Projects projects = projectRepository.findById(eventDTO.getProjectDTO().getProjectId()).get();
        Event event = new Event();
        BeanUtils.copyProperties(eventDTO, event);
        event.setCreateDate(LocalDateTime.now());
        event.setProjectId(projects.getProjectId());
        event.setStatus(Constants.NORMAL_ACTIVE);
        eventRepository.save(event);
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), Constants.ACTIVE);
        NotificationDto notificationDto = NotificationDto.builder().title(eventDTO.getMeetingTitle()).body(eventDTO.getMeetingDescription()).build();
        List<Long> userId = projectUsersList.stream().map(ProjectUsers::getUserId).collect(Collectors.toList());
        List<Notification> notificationEntities =new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(userId)){
            userId.forEach(o->{
                notificationEntities.add(Notification.builder()
                        .projectId(eventDTO.getProjectDTO().getProjectId())
                        .userId(o)
                        .title(eventDTO.getMeetingTitle())
                        .description(eventDTO.getMeetingDescription())
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
            });
            MessageDto messageDto =  MessageDto.builder().notification(notificationDto).userId(userId).build();
            fireBaseService.sendManyNotification(messageDto);
            notificationRepository.saveAll(notificationEntities);
        }
        return event;
    }

    @Override
    public Event updateEvent(Event event) throws FirebaseMessagingException {
        Assert.notNull(event.getMeetingTitle(),"MeetingTitle must not be null");
        Assert.notNull(event.getStartDate(),"StartDate must not be null");
        Assert.notNull(event.getMeetingType(),"MeetingType must not be null");
        Assert.notNull(event.getProjectId(),"Project must not be null");
        if(Objects.equals(Constants.OFFLINE_ROOM, event.getMeetingType())){
            Assert.notNull(event.getEndDate(),"EndDate must not be null");
            Event event1 = eventRepository.findEventWithStartTime(event.getStartDate());
            Assert.notNull(event1,"This meeting room already has a group, please choose another meeting room");
            Assert.notNull(event.getRoomMeeting(),"RoomMeeting must not be null");
        }else if (Objects.equals(Constants.ONLINE_ROOM, event.getMeetingType())){
            Assert.notNull(event.getLinkMeeting(),"Link meeting must not be null");
        }
        event.setCreateDate(LocalDateTime.now());
        eventRepository.save(event);
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(event.getProjectId(), Constants.ACTIVE);
        String title="";
        String des="";
        if(event.getStatus()==0){
            title="Meeting canceled";
            des="Meeting at "+ event.getStartDate()+"  o'clock has been canceled";
        }else{
            title="The meeting has been changed";
            des="The meeting has been changed";
        }
        NotificationDto notificationDto = NotificationDto.builder().title(title).body(des).build();
        List<Long> userId = projectUsersList.stream().map(ProjectUsers::getUserId).collect(Collectors.toList());
        List<Notification> notificationEntities =new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(userId)){
            String finalTitle = title;
            String finalDes = des;
            userId.forEach(o->{
                notificationEntities.add(Notification.builder()
                        .projectId(event.getProjectId())
                        .userId(o)
                        .title(finalTitle)
                        .description(finalDes)
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
            });
            MessageDto messageDto =  MessageDto.builder().notification(notificationDto).userId(userId).build();
            fireBaseService.sendManyNotification(messageDto);
            notificationRepository.saveAll(notificationEntities);
        }
        return event;
    }

    @Override
    public EventDTO getDetailEvent(Long scheduleId) {
        Assert.notNull(scheduleId,"EventId must not be null");
        Event event = eventRepository.findById(scheduleId).get();
        Assert.notNull(event,"Not found event with id "+ scheduleId);
        ProjectDTO p = projectService.getDetailProject(event.getProjectId());
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(event, eventDTO);
        eventDTO.setProjectDTO(p);
        return eventDTO;
    }
//    @Autowired
//    private ProjectJob projectJob;
    @Override
    public ObjectPaging searchEvent(ObjectPaging objectPaging) {
      //  projectJob.checkDeadlineProjectAndUpdatePriority();
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("event_id").descending());
        Page<Event> list= eventRepository.searchEvent(objectPaging.getProjectId(),objectPaging.getStatus(),objectPaging.getFromDate(),objectPaging.getToDate(),pageable);
        List<EventDTO> eventDTOS =new ArrayList<>();
        list.getContent().forEach(o->{
            ProjectDTO p = projectService.getDetailProject(o.getProjectId());
            EventDTO eventDTO = new EventDTO();
            BeanUtils.copyProperties(o, eventDTO);
            eventDTO.setProjectDTO(p);
            eventDTOS.add(eventDTO);
        });
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(eventDTOS).build();
    }
}
