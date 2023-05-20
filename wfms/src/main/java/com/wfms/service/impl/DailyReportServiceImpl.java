package com.wfms.service.impl;

import com.wfms.Dto.*;
import com.wfms.config.Const;
import com.wfms.entity.DailyReport;
import com.wfms.entity.Projects;
import com.wfms.entity.Task;
import com.wfms.entity.Users;
import com.wfms.repository.DailyReportRepository;
import com.wfms.repository.ProjectRepository;
import com.wfms.repository.TaskRepository;
import com.wfms.service.DailyReportService;
import com.wfms.service.ProjectService;
import com.wfms.service.TaskService;
import com.wfms.service.UsersService;
import com.wfms.utils.JwtUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import java.time.LocalDateTime;
import java.util.*;

@Service
public class DailyReportServiceImpl implements DailyReportService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private DailyReportRepository dailyReportRepository;
    public List<DailyReport> findAll(){
        return dailyReportRepository.findAll();
    }


    @Override
    public Page<DailyReport> findWithPage(int total, int page) {
        Pageable pageableRequest = PageRequest.of(page, total, Sort.Direction.DESC);
        return dailyReportRepository.findAll(pageableRequest);
    }

    @Override
    public List<DailyReport> findByProjectId(Long projectId) {
        return dailyReportRepository.findByProjectId(projectId);
    }

    @Override
    public DailyReportDTO createDailyReport(String token, DailyReportDTO dailyReport) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(dailyReport.getProjects(), Const.responseError.projectId_null);
        Projects projects = projectRepository.getById(dailyReport.getProjects().getProjectId());
        Assert.notNull(projects,"Not found project with ID "+dailyReport.getProjects().getProjectId());
        Assert.notNull(dailyReport.getTask(),Const.responseError.taskId_null);
        Task task = taskRepository.findById(dailyReport.getTask().getTaskId()).get();
        Assert.notNull(task,"Not found task with ID "+dailyReport.getTask().getTaskId());
//        DailyReport d = dailyReportRepository.getLastDailyOfUser(dailyReport.getProjects().getProjectId(),users.getId());
//        if(Objects.nonNull(d)){
//            Calendar c1 = Calendar.getInstance();
//            Calendar c2 = Calendar.getInstance();
//            c1.setTime(LocalDateTime.now());
//            c2.setTime(d.getCreateDate());
//            int day=1000*60*60*24;
//            Assert.isTrue(!(c1.getTimeInMillis()/day==c2.getTimeInMillis()/day),"Bạn đã tạo daily report ngày hôm nay");
//        }
        DailyReport dailyReport1=new DailyReport();
        BeanUtils.copyProperties(dailyReport,dailyReport1);
        dailyReport1.setProjects(projects);
        dailyReport1.setTaskId(task.getTaskId());
        dailyReport1.setCreateDate(LocalDateTime.now());
        dailyReport1.setStatus(1);
        dailyReport1.setMemberDoWork(users.getId());
        dailyReportRepository.save(dailyReport1);
        return dailyReport;
    }

    @Override
    public DailyReportDTO getDetailDailyReport(Long dailyReportId) {
        Assert.notNull(dailyReportId,Const.responseError.dailyReportId_null);
        DailyReport d=dailyReportRepository.findById(dailyReportId).get();
        Assert.notNull(d,Const.responseError.dailyReport_notFound+dailyReportId);
        DailyReportDTO dailyReportDTO= new DailyReportDTO();
        BeanUtils.copyProperties(d,dailyReportDTO);
        UsersDto u =usersService.getUserById(d.getMemberDoWork());
        dailyReportDTO.setMemberDoWork(u);
        ProjectDTO p = projectService.getDetailProject(d.getProjects().getProjectId());
        dailyReportDTO.setProjects(p);
        TaskDTO t =taskService.getDetailTaskById(d.getTaskId());
        dailyReportDTO.setTask(t);
        return dailyReportDTO;
    }

    @Override
    public DailyReport updateDailyReport(DailyReport dailyReport) {
        Assert.notNull(dailyReport.getDailyReportId(),Const.responseError.dailyReportId_null);
        DailyReport d=dailyReportRepository.findById(dailyReport.getDailyReportId()).get();
        Assert.notNull(d,Const.responseError.dailyReport_notFound+dailyReport.getDailyReportId());
        dailyReport.setCreateDate(d.getCreateDate());
        dailyReport.setMemberDoWork(d.getMemberDoWork());
        dailyReport.setProjects(d.getProjects());
        BeanUtils.copyProperties(dailyReport,d);
        d.setUpdateDate(LocalDateTime.now());
        return dailyReportRepository.save(d);
    }

    @Override
    public ObjectPaging searchDaiLyReport(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("dailyReportId").descending());
        Page<DailyReport> list = dailyReportRepository.searchDailyReport(objectPaging.getProjectId(),objectPaging.getUserId(),objectPaging.getDate(),objectPaging.getTaskId(),objectPaging.getStatus(),pageable);
        List<DailyReportDTO> dailyReportDTOS=new ArrayList<>();
        list.getContent().forEach(o->{
            DailyReportDTO d=new DailyReportDTO();
            BeanUtils.copyProperties(o,d);
            UsersDto u =usersService.getUserById(o.getMemberDoWork());
            d.setMemberDoWork(u);
            TaskDTO t = taskService.getDetailTaskById(o.getTaskId());
            d.setTask(t);
            ProjectDTO p = projectService.getDetailProject(o.getProjects().getProjectId());
            d.setProjects(p);
            dailyReportDTOS.add(d);
        });
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(dailyReportDTOS).build();
    }

}
