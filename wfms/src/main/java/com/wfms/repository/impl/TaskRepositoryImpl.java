package com.wfms.repository.impl;

import com.wfms.Dto.*;
import com.wfms.entity.Users;
import com.wfms.repository.TaskRepositoryCustom;
import com.wfms.service.TaskService;
import com.wfms.service.UsersService;
import com.wfms.utils.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public List<ReportUserTaskDTO> getReportUserTask(Long projectId, Boolean checkDoing) {
        List<Integer> list= new ArrayList<>(List.of(3));
        if(!checkDoing){
            list.add(2);
        }
        String queryStr = "select tu.user_id,w.work_flow_step_id,w.work_flow_step_name,count(w.work_flow_step_name) from task t \n" +
                "join task_users tu on t.task_id = tu.task_id\n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id\n" +
                "where t.status in (:status) and tu.status =2 and tu.user_id in(\n" +
                "select p.user_id from project_users p where p.project_id = :projectId  and p.status =1 )\n" +
                "group by tu.user_id,w.work_flow_step_name,w.work_flow_step_id\n";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("projectId", projectId);
        query.setParameter("status", list);
        String queryStr2 = "select tu.user_id,l.level_difficult_id, l.level_difficult_name,count(l.level_difficult_name)  from task t \n" +
                "join task_users tu on t.task_id = tu.task_id\n" +
                "join level_difficult l on t.level_difficult_id = l.level_difficult_id\n" +
                "where t.status in (:status) and tu.status =2 and tu.user_id in(\n" +
                "select p.user_id from project_users p where p.project_id = :projectId  and p.status =1 )\n" +
                "group by tu.user_id,l.level_difficult_name,l.level_difficult_id";
        Query query2 = em.createNativeQuery(queryStr2);
        query2.setParameter("projectId", projectId);
        query2.setParameter("status", list);
        return parseResultReportUserTask(query.getResultList(),query2.getResultList());
    }

    @Override
    public List<TaskDoingDTO> getTaskDoing(Long userId) {
        String queryStr = "select  w.work_flow_step_id, w.work_flow_step_name ,count(w.work_flow_step_name) from task t join task_users tu on t.task_id = tu.task_id\n" +
                "join work_flow_step w on t.work_flow_step_id=w.work_flow_step_id \n" +
                "where t.status = 3 and tu.status =2 and  tu.user_id= :userId group by w.work_flow_step_name,w.work_flow_step_id";
        Query query = em.createNativeQuery(queryStr);
        query.setParameter("userId", userId);
        return parseResultDoingTask(query.getResultList());
    }

    @Override
    public List<ChartTask> getTaskInProject(Long projectId) {
        String queryStr = "  select  w.work_flow_step_name ,\n" +
                "  count(CASE WHEN t.work_flow_step_id=w.work_flow_step_id  THEN 0 END) \n" +
                "  from work_flow_step w\n" +
                " left join  task t  on t.work_flow_id=w.work_flow_id \n" +
                " where w.work_flow_id = \n" +
                " (select wl.work_flow_id from work_flow wl where project_id = :projectId )\n" +
                " and w.status =1 \n" +
                "group by w.work_flow_step_name ,w.step\n" +
                "ORDER BY w.step asc;";
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

    @Override
    public List<DashBoard> dashBoard() {
        String queryStr = "select  u.* , (select count(t1.task_id) from \n" +
                " task t1 join task_users tu1 on t1.task_id= tu1.task_id\n" +
                "where t1.dead_line < t1.archived_date and tu1.user_id = tu.user_id  and\n" +
                "\t\t\t\t\t tu1.status =2 and t1.status =2 ) as saiHan,\n" +
                "\t\t\t\t\t  (select count(t1.task_id) from \n" +
                " task t1 join task_users tu1 on t1.task_id= tu1.task_id\n" +
                "where t1.dead_line > t1.archived_date and tu1.user_id = tu.user_id  and\n" +
                "\t\t\t\t\t tu1.status =2 and t1.status =2 ) as dungHan,\n" +
                "COUNT(tu.task_users_id) filter (where tu.is_responsible = true) as main,\n" +
                "COUNT(t.task_id) as member\n" +
                "from task t join task_users tu on t.task_id = tu.task_id \n" +
                "join users u on u.id=tu.user_id where tu.status =2 and t.status =2 and u.status =1 \n" +
                " group by tu.user_id,u.id;";
        Query query = em.createNativeQuery(queryStr);
        return parseResultDashBoard(query.getResultList());
    }
    private List<DashBoard> parseResultDashBoard(List<Object[]> lst){
        List<DashBoard> chartResponseDtos = new ArrayList<>();
        for(Object[] item:lst){
            chartResponseDtos.add(DashBoard.builder()
                    .userId(Long.valueOf(item[0].toString()))
                            .username(item[13].toString())
                            .fullName(item[6].toString())
                            .emailAddress(item[4].toString())
                            .address(item[1].toString())
                            .phone(item[10].toString())
                            .jobTitle(item[8].toString())
                            .gender(Integer.parseInt(item[7].toString()))
                            .birthDay((item[2].toString()))
                    .missTime(Integer.parseInt(item[14].toString()))
                    .ontime(Integer.parseInt(item[15].toString()))
                    .numberTaskIsMain(Integer.parseInt(item[16].toString()))
                    .numberTaskIsMember(Integer.parseInt(item[17].toString()))
                    .build());
        }
        return chartResponseDtos;
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
                    .stepId(Long.valueOf(item[1].toString()))
                    .step(item[2].toString())
                    .countStep(Integer.valueOf(item[3].toString()))
                    .build());
        }
        List<ReportTaskLevelDTO> chartResponseLevel = new ArrayList<>();

        for(Object[] item:lst2){
            chartResponseLevel.add(ReportTaskLevelDTO.builder()
                    .userId(Long.valueOf(item[0].toString()))
                    .levelId(Long.valueOf(item[1].toString()))
                    .level(item[2].toString())
                    .countLevel(Integer.valueOf(item[3].toString()))
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
