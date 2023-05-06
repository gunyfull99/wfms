package com.wfms.repository.impl;

import com.wfms.Dto.*;
import com.wfms.entity.Users;
import com.wfms.repository.TaskRepositoryCustom;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UsersService usersService;
    @Override
    public List<ChartResponseDto> getstatisticTask(Long projectId) {
        String queryStr = "select \n" +
                "        EXTRACT('MONTH' FROM iss.created_date) as months,\n" +
                "        count(task_id) as totalTask,\n" +
                "        sum(case when iss.is_archived = 'true' then 1 else 0 end) as taskDone,\n" +
                "        sum(case when iss.is_archived = 'false' then 1 else 0 end) as taskNotDone\n" +
                "        from task iss where iss.project_id = :projectId group by EXTRACT('MONTH' FROM iss.created_date) \n" +
                "        order by EXTRACT('MONTH' FROM iss.created_date) desc";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        return parseResult(query.getResultList());
    }

    @Override
    public List<ReportUserTaskDTO> getReportUserTask(Long projectId) {
        String queryStr = "select tu.user_id,w.work_flow_step_name,count(w.work_flow_step_name) from task t \n" +
                "join task_users tu on t.task_id = tu.task_id\n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id\n" +
                "where tu.user_id in(\n" +
                "select p.user_id from project_users p where p.project_id = :projectId  and p.status =1 )\n" +
                "group by tu.user_id,w.work_flow_step_name\n";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        String queryStr2 = "select tu.user_id, l.level_difficult_name,count(l.level_difficult_name)  from task t \n" +
                "join task_users tu on t.task_id = tu.task_id\n" +
                "join level_difficult l on t.level_difficult_id = l.level_difficult_id\n" +
                "where tu.user_id in(\n" +
                "select p.user_id from project_users p where p.project_id = :projectId  and p.status =1 )\n" +
                "group by tu.user_id,l.level_difficult_name";
        Query query2 = em.createNativeQuery(queryStr2);
        query2.setParameter("projectId", projectId);
        return parseResultReportUserTask(query.getResultList(),query2.getResultList());
    }

    @Override
    public List<TaskDoingDTO> getTaskDoing(Long userId) {
        String queryStr = "select w.work_flow_step_id, w.work_flow_step_name ,count(w.work_flow_step_name) from task t join task_users tu on t.task_id = tu.task_id\n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id \n" +
                "where tu.user_id= :userId group by w.work_flow_step_name,w.work_flow_step_id";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("userId", userId);
        return parseResultDoingTask(query.getResultList());
    }

    @Override
    public List<ChartTask> getTaskInProject(Long projectId) {
        String queryStr = "select  w.work_flow_step_name ,count(w.work_flow_step_name) from task t  \n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id \n" +
                "where t.sprint_id in (\n" +
                "select sprint_id from sprint where project_id = :projectId and status != 0\n" +
                ") group by w.work_flow_step_name";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        return parseResultTaskInProject(query.getResultList());
    }

    @Override
    public List<ChartTask> getTaskInProjectWithStatus(Long projectId, Integer status) {
        String queryStr = "select  w.work_flow_step_name ,count(w.work_flow_step_name) from task t  \n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id \n" +
                "where t.sprint_id in (\n" +
                "select sprint_id from sprint where project_id = :projectId and status = :status \n" +
                ") group by w.work_flow_step_name";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        query.setParameter("status", status);
        return parseResultTaskInProject(query.getResultList());
    }

    private List<ChartTask> parseResultTaskInProject(List<Object[]> lst){
        List<ChartTask> chartResponseDtos = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseDtos.add(ChartTask.builder()
                    .name((item[0].toString()))
                    .numberTask(Integer.parseInt(item[1].toString()))
                    .build());
        }
        return chartResponseDtos;
    }
    private List<TaskDoingDTO> parseResultDoingTask(List<Object[]> lst){
        List<TaskDoingDTO> chartResponseDtos = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseDtos.add(TaskDoingDTO.builder()
                    .stepId(Long.valueOf(item[0].toString()))
                    .stepName(item[1].toString())
                    .numberTask(Integer.valueOf(item[2].toString()))
                    .build());
        }
        return chartResponseDtos;
    }
    private List<ChartResponseDto> parseResult(List<Object[]> lst){
        List<ChartResponseDto> chartResponseDtos = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseDtos.add(ChartResponseDto.builder()
                            .month(Double.valueOf(item[0].toString()))
                            .totalTask(Double.valueOf(item[1].toString()))
                            .totalTaskDone(Double.valueOf(item[2].toString()))
                            .totalTaskNotDone(Double.valueOf(item[3].toString()))
                            .build());
        }
        return chartResponseDtos;
    }

    private List<ReportUserTaskDTO> parseResultReportUserTask(List<Object[]> lst,List<Object[]> lst2){
        List<ReportTaskStepDTO> chartResponseStep = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseStep.add(ReportTaskStepDTO.builder()
                    .userId(Long.valueOf(item[0].toString()))
                    .step(item[1].toString())
                    .countStep(Integer.valueOf(item[2].toString()))
                    .build());
        }
        List<ReportTaskLevelDTO> chartResponseLevel = new ArrayList<>();

        for(Object[] item:lst2){
            chartResponseLevel.add(ReportTaskLevelDTO.builder()
                    .userId(Long.valueOf(item[0].toString()))
                    .level(item[1].toString())
                    .countLevel(Integer.valueOf(item[2].toString()))
                    .build());
        }
        List<ReportUserTaskDTO> chartResponse = new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(chartResponseStep)){

        chartResponseStep.forEach(o->{
            if(DataUtils.listNotNullOrEmpty(chartResponse)){
                List<UsersDto> u=chartResponse.stream().map(ReportUserTaskDTO ::getUser).collect(Collectors.toList());
                List<UsersDto> a =u.stream().filter(t-> Objects.equals(t.getId(), o.getUserId())).collect(Collectors.toList());
            if(DataUtils.listNotNullOrEmpty(a)){
                for (int i = 0; i <chartResponse.size() ; i++) {
                    if(Objects.equals(chartResponse.get(i).getUser().getId(), o.getUserId())){
                        chartResponse.get(i).getReportTaskStep().add(o);
                    }
                }
            }else{
                Users udto = usersService.getById(o.getUserId());
                UsersDto ud = new UsersDto();
                BeanUtils.copyProperties(udto, ud);
                List<ReportTaskStepDTO> chartResponseStep1=new ArrayList<>();
                chartResponseStep1.add(o);
                chartResponse.add(ReportUserTaskDTO.builder()
                        .user(ud).
                        reportTaskLevel(new ArrayList<>()).
                        reportTaskStep(chartResponseStep1).
                        build());
            }
            }else{
                Users udto = usersService.getById(o.getUserId());
                UsersDto ud = new UsersDto();
                BeanUtils.copyProperties(udto, ud);
                List<ReportTaskStepDTO> chartResponseStep1=new ArrayList<>();
                chartResponseStep1.add(o);
                chartResponse.add(ReportUserTaskDTO.builder()
                        .user(ud).
                        reportTaskLevel(new ArrayList<>()).
                        reportTaskStep(chartResponseStep1).
                        build());
            }
        });
        }
        if(DataUtils.listNotNullOrEmpty(chartResponseLevel)) {
            chartResponseLevel.forEach(o -> {
                if (DataUtils.listNotNullOrEmpty(chartResponse)) {
                    List<UsersDto> u = chartResponse.stream().map(ReportUserTaskDTO::getUser).collect(Collectors.toList());
                    List<UsersDto> a = u.stream().filter(t -> Objects.equals(t.getId(), o.getUserId())).collect(Collectors.toList());
                    if (DataUtils.listNotNullOrEmpty(a)) {
                        for (int i = 0; i < chartResponse.size(); i++) {
                            if (Objects.equals(chartResponse.get(i).getUser().getId(), o.getUserId())) {
                                chartResponse.get(i).getReportTaskLevel().add(o);
                            }
                        }
                    } else {
                        Users udto = usersService.getById(o.getUserId());
                        UsersDto ud = new UsersDto();
                        BeanUtils.copyProperties(udto, ud);
                        List<ReportTaskLevelDTO> chartResponseStep1 = new ArrayList<>();
                        chartResponseStep1.add(o);
                        chartResponse.add(ReportUserTaskDTO.builder()
                                .user(ud).
                                reportTaskLevel(chartResponseStep1).
                                reportTaskStep(new ArrayList<>()).
                                build());
                    }
                } else {
                    Users udto = usersService.getById(o.getUserId());
                    UsersDto ud = new UsersDto();
                    BeanUtils.copyProperties(udto, ud);
                    List<ReportTaskLevelDTO> chartResponseStep1 = new ArrayList<>();
                    chartResponseStep1.add(o);
                    chartResponse.add(ReportUserTaskDTO.builder()
                            .user(ud).
                            reportTaskLevel(chartResponseStep1).
                            reportTaskStep(new ArrayList<>()).
                            build());
                }
            });
        }
        return chartResponse;
    }
}
