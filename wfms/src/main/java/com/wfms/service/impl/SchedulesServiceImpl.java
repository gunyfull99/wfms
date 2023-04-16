package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.MessageDto;
import com.wfms.Dto.NotificationDto;
import com.wfms.Dto.SchedulesDTO;
import com.wfms.entity.Projects;
import com.wfms.entity.Schedules;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.ProjectUsersRepository;
import com.wfms.repository.SchedulesRepository;
import com.wfms.service.FireBaseService;
import com.wfms.service.SchedulesService;
import com.wfms.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import  com.wfms.entity.ProjectUsers;

@Service
@Slf4j
public class SchedulesServiceImpl implements SchedulesService {
    @Autowired
    private SchedulesRepository schedulesRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Override
    public Schedules createSchedules(SchedulesDTO schedulesDTO) throws FirebaseMessagingException {
        Assert.notNull(schedulesDTO.getMeetingTitle(),"Tiêu đề cuộc họp không được để trống");
        Assert.notNull(schedulesDTO.getStartDate(),"Thời gian bắt đầu cuộc họp không được để trống");
        Assert.notNull(schedulesDTO.getStartDate(),"Thời gian bắt đầu cuộc họp không được để trống");
        Assert.notNull(schedulesDTO.getEndDate(),"Thời gian kết thúc cuộc họp không được để trống");
        Assert.notNull(schedulesDTO.getMeetingType(),"Loại cuộc họp không được để trống (online hoặc offline)");
        if(Constants.OFFLINE_ROOM == schedulesDTO.getMeetingType()){
            Schedules schedules = schedulesRepository.findSchedulesWithStartTime(schedulesDTO.getStartDate());
            Assert.notNull(schedules,"Đã có nhóm đăng ký phòng họp này, hãy chọn phòng họp khác");
            Assert.notNull(schedulesDTO.getRoomMeeting(),"Phòng họp không được để trống");
        }else if (Constants.ONLINE_ROOM == schedulesDTO.getMeetingType()){
            Assert.notNull(schedulesDTO.getLinkMeeting(),"Link họp không được để trống");
        }
        Projects projects = projectRepository.getById(schedulesDTO.getProjectId());
        Schedules schedules = new Schedules();
        BeanUtils.copyProperties(schedulesDTO,schedules);
        schedules.setCreateDate(new Date());
        schedules.setProjectId(projects.getProjectId());
        schedules.setStatus(Constants.NORMAL_ACTIVE);
        schedulesRepository.save(schedules);
        List<ProjectUsers> projectUsersList = projectUsersRepository.findAllByProjectIdAndStatus(projects.getProjectId(), Constants.ACTIVE);
        NotificationDto notificationDto = NotificationDto.builder().title(schedulesDTO.getMeetingTitle()).body(schedulesDTO.getMeetingDescription()).build();
        List<Long> userId = projectUsersList.stream().map(i->i.getUserId()).collect(Collectors.toList());
        MessageDto messageDto =  MessageDto.builder().notification(notificationDto).userId(userId).build();
        fireBaseService.sendManyNotification(messageDto);
        return schedules;
    }
}
