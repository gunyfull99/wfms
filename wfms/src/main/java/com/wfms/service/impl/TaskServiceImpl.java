package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.config.Const;
import com.wfms.entity.*;
import com.wfms.job.ProjectJob;
import com.wfms.repository.*;
import com.wfms.service.*;
import com.wfms.utils.DataUtils;
import com.wfms.utils.JwtUtility;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
 import java.time.LocalDateTime; 
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectUsersRepository projectUsersRepository;
    @Autowired
    private TaskUsersRepository taskUsersRepository;
    @Autowired
    private TaskUsersService taskUsersService;
    @Autowired
    private WorkFlowRepository workFlowRepository;
    @Autowired
    private WorkFlowStepRepository workFlowStepRepository;
    @Autowired
    private PriorityRepository priorityRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private TaskTypeRepository taskTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkFlowStepService workFlowStepService;
    @Autowired
    private SprintService sprintService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private JwtUtility jwtUtility;
    @Autowired
    private RequestTaskRepository requestTaskRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FireBaseService fireBaseService;
    @Override

    public List<Task> getTaskByUserId(Long userId) {
        return taskRepository.getTaskByUserId(userId);
    }

    @Override
    public List<TaskDoingDTO> getTaskDoing(String token) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<TaskDoingDTO> list=taskRepository.getTaskDoing(users.getId());
         list.forEach(o->{
             List<Task> taskList=taskRepository.getListTaskByUserAndStep(o.getStepId(),users.getId());
             List<TaskDTO> t=convert(taskList);
             o.setTask(t);
         });
        return list;
    }

    @Override
    public List<TaskDTO> getTaskByUserIdAndProjectId(Long userId, Long projectId) {
        Assert.notNull(userId, Const.responseError.userId_null);
        Assert.notNull(projectId,Const.responseError.projectId_null);
        List<Task> list = taskRepository.getTaskByUserIdAndProjectId(userId,projectId);
        return convert(list);
    }

    @Override
    public Task createTask(String token, TaskDTO task) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(task.getProjectId(),Const.responseError.projectId_null);
        Assert.notNull(task.getDeadLine(),Const.responseError.deadline_null);
        Assert.notNull(task.getPriorityId(),Const.responseError.priorityId_null);
        Assert.notNull(task.getLevelDifficultId(),"Difficult level must not be null");
        Assert.notNull(task.getTaskTypeId(),"Task type must not be null");
        Projects p = projectRepository.findById(task.getProjectId()).get();
        Assert.notNull(p,Const.responseError.project_notFound+ task.getProjectId());
        if(p.getStatus()==2){
            Assert.isTrue(false,"Project closed");
        }else if(p.getStatus()==1){
            Assert.isTrue(false,"Project not start");
        }else if(p.getStatus()==0){
            Assert.isTrue(false,"Project inactive");
        }
        Priority priority=priorityRepository.findById(task.getPriorityId()).get();
        Assert.notNull(priority,Const.responseError.priority_notFound+ task.getPriorityId());
        LevelDifficult level=levelRepository.findById(task.getLevelDifficultId()).get();
        Assert.notNull(level,"Not found difficult level with ID"+ task.getLevelDifficultId());
        TaskTypes taskTypes = taskTypeRepository.findById(task.getTaskTypeId()).get();
        Assert.notNull(taskTypes,"Not found task type with ID "+ task.getTaskTypeId());
        WorkFlow workFlow=workFlowRepository.getDetailWorkflow(task.getProjectId());
        Assert.notNull(workFlow,"Not found workflow ");
        Integer count = taskRepository.getCountTaskByProject(p.getProjectId());
        Task i = new Task();
        BeanUtils.copyProperties(task,i);
        i.setCreatedDate(LocalDateTime.now());
        i.setTaskId(null);
        i.setWorkFlowId(workFlow.getWorkFlowId());
        i.setCode(p.getShortName()+"-"+ (count==null ? 1:count+1));
        i.setArchivedBy(null);
        i.setArchivedDate(null);
        i.setIsArchived(false);
        if(Objects.nonNull(task.getParent())&& Objects.nonNull(task.getParent().getTaskId())){
            Task taskData = taskRepository.getById(task.getParent().getTaskId());
            Assert.notNull(taskData,Const.responseError.task_notFound+task.getParent().getTaskId());
            i.setParent(taskData.getTaskId());
        }

        WorkFlowStep workFlowStep = workFlowStepRepository.getWorkFLowStepStart(workFlow.getWorkFlowId());
        Assert.notNull(workFlowStep,"Not found step start ");
        i.setWorkFlowStepId(workFlowStep.getWorkFlowStepId());
        if(Objects.nonNull(task.getSprintId())){
            SprintDTO sprintDTO=sprintService.getDetailSprint(task.getSprintId());
            Assert.isTrue(sprintDTO.getStatus()!=2,"Sprint was closed ");
            i.setSprint(Sprint.builder().sprintId(sprintDTO.getSprintId()).build());
        }
        i.setPriority(Priority.builder().priorityId(task.getPriorityId()).build());
        i.setLevelDifficultId(task.getLevelDifficultId());
        UsersDto u =new UsersDto();
        if(role.contains("PM")){
            i.setCreateByPm(true);
            if(Objects.nonNull(task.getAssigness())) {
                u = usersService.getUserById(task.getAssigness());
                i.setStatus(!u.getJobTitle().equals("ADMIN")? 3 : 1);
                i.setApproveDate(LocalDateTime.now());
            }else{
                i.setStatus(1);
            }
        }else{
            i.setAssigness(users.getId());
            i.setCreateByPm(false);
            i.setStatus(1);

        }
        i.setReporter(users.getId());
        i = taskRepository.save(i);
        List<Notification> notificationEntities =new ArrayList<>();
        if(Objects.nonNull(task.getAssigness())){
            u = usersService.getUserById(task.getAssigness());
            if(role.contains("PM")){
                taskUsersService.createTaskUser(TaskUsers.builder()
                        .taskId(i.getTaskId())
                        .userId(task.getAssigness())
                        .status(2)
                        .isResponsible(true).
                      //  .isTesterResponsible(u.getJobTitle().equals("TESTER")).
                        build());

                notificationEntities.add(Notification.builder()
                        .taskId(i.getTaskId())
                        .userId(task.getAssigness())
                        .title("Add to task "+i.getCode())
                        .description("You have been added to the task "+i.getCode())
                        .status(1)
                        .timeRecive(LocalDateTime.now())
                        .createDate(LocalDateTime.now())
                        .build());
                MessageDto messageDtoList =   MessageDto.builder().userId(List.of(i.getAssigness()))
                        .notification(NotificationDto.builder().taskId(i.getTaskId()).title("Add to task "+i.getCode()).body("You have been added to the task "+i.getCode()).build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
                notificationRepository.saveAll(notificationEntities);
            }else{
                taskUsersService.createTaskUser(TaskUsers.builder()
                        .taskId(i.getTaskId())
                        .userId(users.getId())
                        .status(1)
                        .isResponsible(true)
                      //  .isTesterResponsible(u.getJobTitle().equals("TESTER"))
                        .build());
            }
        }
        return i;
    }
    @Override
    public List<TaskDTO> getTaskByProjectId(Long projectId) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
        List<Task> task = taskRepository.getTaskByProjectId(projectId);
        return convert(task);
    }

    @Override
    public TaskDTO getDetailTaskById(Long taskId) {
        Assert.notNull(taskId,Const.responseError.taskId_null);
        Task task = taskRepository.findById(taskId).get();
        Assert.notNull(task,Const.responseError.task_notFound+taskId);
        return convert(List.of(task)).get(0);
    }

    @Override
    public Task updateTask(String token, TaskDTO task) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(task.getTaskId(),Const.responseError.taskId_null);
        Task taskData = taskRepository.getById(task.getTaskId());
        Assert.notNull(taskData,Const.responseError.task_notFound+task.getTaskId());
        List<WorkFlowStep> workFlowSteps =workFlowStepService.listWorkFlowStep(taskData.getWorkFlowId());
        List<Long> workFlowStep=workFlowSteps
                        .stream().map(WorkFlowStep :: getWorkFlowStepId).collect(Collectors.toList());
        Assert.isTrue(workFlowStep.contains(task.getWorkFlowStepId()),"WorkFlowStep is not in the current WorkFlow");
        List<WorkFlowStep> stepResolve=workFlowSteps.stream().filter(WorkFlowStep::getResolve).collect(Collectors.toList());
        List<WorkFlowStep> stepClose=workFlowSteps.stream().filter(WorkFlowStep::getClosed).collect(Collectors.toList());
      //  List<WorkFlowStep> stepTested=workFlowSteps.stream().filter(WorkFlowStep::getTested).collect(Collectors.toList());
     //   ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(taskData.getAssigness(),taskData.getProjectId());
      //  Assert.notNull(projectUsers,"Không tìm thấy người làm công việc nên không thể chuyển công việc");
        if(task.getSprintId()!=null){
          List<Long>sprintId= sprintService.findSprintByProjectId(taskData.getProjectId())
                  .stream().map(SprintDTO::getSprintId).collect(Collectors.toList());
          Assert.isTrue(sprintId.contains(task.getSprintId()),"Sprint is not in the current project");
            List<SprintDTO>sprintdto= sprintService.findSprintByProjectId(taskData.getProjectId())
                    .stream().filter(o-> Objects.equals(o.getSprintId(), task.getSprintId())).collect(Collectors.toList());
            Assert.isTrue(sprintdto.get(0).getStatus()!=2,"Sprint was closed ");

            taskData.setSprint(Sprint.builder().sprintId(task.getSprintId()).build());
        }
        if(!Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId())){
            if(taskData.getStatus()!=3){
                Assert.isTrue(role.contains("PM"),"Task is not active.You do not have permission to perform update step");
            }
        }
        boolean checkChange=false;
        boolean checkChangeStep=false;
        boolean checkStepDone=false;
        List<Long>userId=new ArrayList<>();

        if(role.contains("PM") || role.contains("pm")){
            taskData.setDescription(task.getDescription());
            taskData.setSummary(task.getSummary());
            if(Objects.nonNull(task.getStatus())){
                taskData.setStatus(task.getStatus());
            }
            if(Objects.nonNull(task.getParent()) && Objects.nonNull(task.getParent().getTaskId())
            && !Objects.equals(taskData.getParent(), task.getParent().getTaskId())){
                taskData.setParent(task.getParent().getTaskId());
            }
            if(Objects.nonNull(task.getTaskTypeId())){
                taskData.setTaskTypeId(task.getTaskTypeId());
            }
            if(Objects.nonNull(task.getLevelDifficultId())){
                taskData.setLevelDifficultId(task.getLevelDifficultId());
            }
            if(Objects.nonNull(task.getDeadLine())){
                taskData.setDeadLine(task.getDeadLine());
            }
            if(Objects.nonNull(task.getPriorityId())){
                taskData.setPriority(Priority.builder().priorityId(task.getPriorityId()).build());
            }
            if(Objects.nonNull(task.getWorkFlowStepId())){
                if(Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId())){
                    taskData.setStatus(2);
                    taskData.setIsArchived(true);
                    taskData.setArchivedDate(LocalDateTime.now());
                    if(!Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId())){
                        checkChange=true;
                    }
                }
                if(!Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId())){
                    checkChangeStep=true;
                }
                taskData.setWorkFlowStepId(task.getWorkFlowStepId());
            }
        }else{
            Assert.isTrue((!Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId())),"You do not have permission to drag to step close task");
            Assert.isTrue((!Objects.equals(stepResolve.get(0).getWorkFlowStepId(), taskData.getWorkFlowStepId()))&& !Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId()),"You do not have permission to drag to other step task");
            TaskUsers tu=taskUsersRepository.findTaskUsersByUserIdAndTaskIdAndStatus(users.getId(),taskData.getTaskId());
           if(Objects.isNull(tu)){
               List<Task>taskList=taskRepository.getListTaskByParent(taskData.getTaskId());
               Assert.isTrue( DataUtils.listNotNullOrEmpty(taskList),"You do not have permission to drag this task");
               List<TaskUsers> ts =new ArrayList<>();
               for (Task value : taskList) {
                   TaskUsers temp = taskUsersRepository.findTaskUsersByUserIdAndTaskIdAndStatus(users.getId(), value.getTaskId());
                   if (Objects.nonNull(temp)) {
                       ts.add(temp);
                   }
                   userId.addAll(taskUsersRepository.findUserInTask(value.getTaskId()));
               }
               Assert.isTrue(DataUtils.listNotNullOrEmpty(ts),"You do not have permission to drag this task" );
           }
            if(!Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId()) && Objects.equals(stepResolve.get(0).getWorkFlowStepId(), taskData.getWorkFlowStepId())){
                checkStepDone=true;
            }
            taskData.setWorkFlowStepId(task.getWorkFlowStepId());
        }
//        else if(users.getJobTitle().contains("DEV")){
//            Assert.isTrue((!Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId()) && (!Objects.equals(stepTested.get(0).getWorkFlowStepId(), task.getWorkFlowStepId()))),"DEV không có quyền kéo sang step này");
//            taskData.setWorkFlowStepId(task.getWorkFlowStepId());
//        }else if(users.getJobTitle().contains("TESTER")){
//            Assert.isTrue(!Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId()) && !Objects.equals(stepResolve.get(0).getWorkFlowStepId(), task.getWorkFlowStepId() ),"TESTER không có quyền kéo sang step này");
//            taskData.setWorkFlowStepId(task.getWorkFlowStepId());
//        }
        taskData.setUpdateDate(LocalDateTime.now());
       Task t= taskRepository.save(taskData);
       if(checkChange||checkChangeStep||checkStepDone){
           List<Notification> notificationEntities =new ArrayList<>();
           String title="";
           String description="";
           if(checkChange){
               userId= taskUsersRepository.findUserInTask(task.getTaskId());
               title="Closed task "+taskData.getCode();
               description="Task "+taskData.getCode()+" have been closed";
           }else if(checkChangeStep){
               userId= taskUsersRepository.findUserInTask(task.getTaskId());
               title="Change step task "+taskData.getCode();
               description="Task "+taskData.getCode()+" have been change step";
           }else{
               userId.add(taskData.getReporter());
               title="Change step task "+taskData.getCode() +" to done";
               description="Task "+taskData.getCode()+" have been to done";
           }
           String finalTitle = title;
           String finalDescription = description;
           if(DataUtils.listNotNullOrEmpty(userId)){
               userId.forEach(o->{
                   notificationEntities.add(Notification.builder()
                           .taskId(t.getTaskId())
                           .userId(o)
                           .title(finalTitle)
                           .description(finalDescription)
                           .status(1)
                           .timeRecive(LocalDateTime.now())
                           .createDate(LocalDateTime.now())
                           .build());
               });
               MessageDto messageDtoList =   MessageDto.builder().userId(userId)
                       .notification(NotificationDto.builder().taskId(t.getTaskId()).title(finalTitle).body(finalDescription).build()).build();
               fireBaseService.sendManyNotification(messageDtoList);
               notificationRepository.saveAll(notificationEntities);
           }
       }

        return t;
    }

    @Override
    public List<TaskUsers> updateAssignessTask(List<TaskUsers> taskUser) throws FirebaseMessagingException {
        if(DataUtils.listNotNullOrEmpty(taskUser)){
            Task taskData = taskRepository.getById(taskUser.get(0).getTaskId());
            Assert.notNull(taskData,Const.responseError.task_notFound+taskUser.get(0).getTaskId());
            List<TaskUsers>countDev=taskUser.stream().filter(TaskUsers::getIsResponsible).collect(Collectors.toList());
            //List<TaskUsers>countTester=taskUser.stream().filter(TaskUsers::getIsTesterResponsible).collect(Collectors.toList());
            if(taskData.getStatus()==1){
                Assert.isTrue(countDev.size()==1 || countDev.size()==0 ,"Number of main must be 1");
             //   Assert.isTrue(countTester.size()==1 || countTester.size()==0 ,"Số TESTER làm chính phải là 1");
            }else{
                Assert.isTrue(countDev.size()==1,"Number of main must be 1");
               // Assert.isTrue(countTester.size()==1 || countTester.size()==0,"Số TESTER làm chính phải là 1");
            }
            List<Long>userIds=new ArrayList<>();
            List<TaskUsers> tu=new ArrayList<>();
//                    taskUser.stream().map(TaskUsers::getUserId).collect(Collectors.toList());
            List<Long>checkTaskId=taskUser.stream().map(TaskUsers::getTaskId).distinct().collect(Collectors.toList());
            Assert.isTrue(checkTaskId.size()==1,"The list of members is not on the same task");
            List<Notification> notificationEntities = new ArrayList<>();
            taskUser.forEach(taskUsers -> {
                Assert.notNull(taskUsers.getTaskId(),Const.responseError.taskId_null);
                Assert.notNull(taskUsers.getUserId(),Const.responseError.userId_null);
                Assert.notNull(taskUsers.getIsResponsible(),"IsResponsible must not be null");
                Assert.notNull(taskUsers.getStatus(),"Status must not be null");
                ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(taskUsers.getUserId(), taskData.getProjectId());
                Assert.notNull(projectUsers,"Can't find members in the project");
                UsersDto usersDto = usersService.getUserById(taskUsers.getUserId());
//                if(taskUsers.getIsResponsible()){
//                    Assert.isTrue(usersDto.getJobTitle().equals("DEV"),"Tester không được làm main của DEV");
//                }
//                if(taskUsers.getIsTesterResponsible()){
//                    Assert.isTrue(usersDto.getJobTitle().equals("TESTER"),"DEV không được làm main của TESTER");
//                }
                TaskUsers taskUsers1 = taskUsersRepository.findTaskUsersByUserIdAndTaskId(taskUsers.getUserId(),taskUsers.getTaskId());
                if(Objects.nonNull(taskUsers1)){

                    if(!Objects.equals(taskUsers1.getStatus(), taskUsers.getStatus())){
                        userIds.add(taskUsers.getUserId());
                        tu.add(taskUsers);
                    }
                    taskUsers1.setStatus(taskUsers.getStatus());
                    taskUsers1.setIsResponsible(taskUsers.getIsResponsible());
                 //   taskUsers1.setIsTesterResponsible(taskUsers.getIsTesterResponsible());
                    taskUsers1.setUpdateDate(LocalDateTime.now());
                    taskUsersRepository.save(taskUsers1);
                    if(taskUsers.getIsResponsible()) {
                        taskData.setAssigness(taskUsers.getUserId());
                    }
                }else{
                    if(taskUsers.getIsResponsible()) {
                        taskData.setAssigness(taskUsers.getUserId());
                    }
                    userIds.add(taskUsers.getUserId());
                    tu.add(taskUsers);
                    taskUsersService.createTaskUser(TaskUsers.builder()
                            .taskId(taskUsers.getTaskId())
                            .userId(taskUsers.getUserId())
                            .status(taskUsers.getStatus())
                            .isResponsible(taskUsers.getIsResponsible())
                         //   .isTesterResponsible(taskUsers.getIsTesterResponsible())
                            .build());
                }
                if(!Objects.equals(taskUsers.getUserId(), taskData.getReporter())){
                    notificationEntities.add(Notification.builder()
                            .taskId(taskUsers.getTaskId())
                            .userId(taskUsers.getUserId())
                            .title(taskUsers.getStatus()==2 ? (taskUsers.getIsResponsible() ? "Main" : "Added")+" to task  "+taskData.getCode() :"Remove from task  "+taskData.getCode() )
                            .description(taskUsers.getStatus()==2 ? "You have been "+ (taskUsers.getIsResponsible() ? "main" : "added") +" to task "+taskData.getCode() :"You have been remove from task "+taskData.getCode())
                            .status(1)
                            .timeRecive(LocalDateTime.now())
                            .createDate(LocalDateTime.now())
                            .build());
                }

            });
            if(taskData.getStatus()==1 && countDev.size()==1){
                taskData.setApproveDate(LocalDateTime.now());
                taskData.setStatus(3);
            }
            taskRepository.save(taskData);
            List<TaskUsers> tu1 = tu.stream().filter(o-> !Objects.equals(o.getUserId(), taskData.getReporter())).collect(Collectors.toList());
            tu1.forEach(taskUsers -> {
                        MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                                .notification(NotificationDto.builder().taskId(taskUsers.getTaskId()).title(taskUsers.getStatus()==2 ? (taskUsers.getIsResponsible() ? "Main" : "Added")+" to task  "+taskData.getCode() :"Remove from task  "+taskData.getCode())
                                        .body(taskUsers.getStatus()==2 ? "You have been "+ (taskUsers.getIsResponsible() ? "main" : "added") +" to task "+taskData.getCode() :"You have been remove from task "+taskData.getCode()).build()).build();
                try {
                    fireBaseService.sendManyNotification(messageDtoList);
                } catch (FirebaseMessagingException e) {
                        Assert.isTrue(true,"Have error in firebase");
                }
            });

            notificationRepository.saveAll(notificationEntities);
        }
        return taskUser;
    }

    @Override
    public List<TaskDTO> getListTask(Long projectId, Long springId) {
        if(Objects.nonNull(springId)){
            List<Task> task = taskRepository.getListTaskInSprint(springId);
            return convert(task);
        }else{
            List<Task> task = taskRepository.getListTaskInBackLog(projectId);
            return convert(task);
        }
    }
    @Override
    public ObjectPaging searchTask(ObjectPaging objectPaging, Boolean isReport) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("taskId").descending());
        Page<Task> list;
        if(Objects.nonNull(objectPaging.getSprintId()) && objectPaging.getSprintId()== -1){
            list = taskRepository.searchTaskPagingBackLog(objectPaging.getProjectId(), objectPaging.getStatus(), Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,objectPaging.getSprintId(),objectPaging.getStepId(),objectPaging.getCreateByPm(),pageable);
        }else if(isReport){
            list = taskRepository.searchTaskPagingWithReport(objectPaging.getProjectId(),
                    objectPaging.getStatus(),
                    Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,
                    objectPaging.getSprintId(),
                    objectPaging.getStepId(),
                    objectPaging.getCreateByPm(),
                    objectPaging.getTaskType(),
                    objectPaging.getPriority(),
                    objectPaging.getUserId(),
                    objectPaging.getLevel(),
                    pageable);
        }else{
            list = taskRepository.searchTaskPaging(objectPaging.getProjectId(),
                    objectPaging.getStatus(),
                    Objects.nonNull(objectPaging.getKeyword()) ? objectPaging.getKeyword().toLowerCase() : null,
                    objectPaging.getSprintId(),
                    objectPaging.getStepId(),
                    objectPaging.getCreateByPm(),
                    objectPaging.getTaskType(),
                    objectPaging.getPriority(),
                    objectPaging.getUserId(),
                    objectPaging.getLevel(),
                    pageable);
        }
        List<TaskDTO> taskDTOList =convert(list.getContent());
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(taskDTOList).build();
    }

    @Override
    public List<ChartTask> chartTask(Long projectId, Integer status) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
        return taskRepository.getTaskInProjectWithStatus(projectId,status);
    }

    @Override
    public DashBoardForPM chartTaskInProject(Long projectId) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
         List<ChartTask> chartTasks= taskRepository.getTaskInProject(projectId);
        int a = 0;
        for (ChartTask o: chartTasks ) {
            a+=o.getNumberTask();
        }
        return DashBoardForPM.builder().
                chartTasks(chartTasks)
                .totalTask(a).build();
    }

    @Override
    public List<DashBoard> chartDashBoard() {
        return taskRepository.dashBoard();

    }

    @Override
    public String requestToTask(String token, RequestTask requestTask) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(requestTask.getTaskId(),Const.responseError.taskId_null);
        Assert.notNull(requestTask.getReason(),"Reason must not be null");
        Assert.notNull(requestTask.getMain(),"Main must not be null");
        Task taskData = taskRepository.findById(requestTask.getTaskId()).get();
        Assert.notNull(taskData,Const.responseError.task_notFound+requestTask.getTaskId());
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(users.getId(), taskData.getProjectId());
        Assert.notNull(projectUsers,"Can't find members " +users.getFullName() +" in this project");
        List<Notification> notificationEntities = new ArrayList<>();
        requestTaskRepository.save(RequestTask.builder()
                .requestTaskId(null)
                .taskId(requestTask.getTaskId())
                .reason(requestTask.getReason())
                .main(requestTask.getMain())
                .userId(users.getId())
                .createDate(LocalDateTime.now())
                .status(1).build());
        notificationEntities.add(Notification.builder()
                .taskId(requestTask.getTaskId())
                .userId(taskData.getReporter())
                .title("Request join task  "+taskData.getCode())
                .description(users.getFullName()+" request join to the task "+taskData.getCode())
                .status(1)
                .timeRecive(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .build());
        MessageDto messageDtoList =   MessageDto.builder().userId(List.of(taskData.getReporter()))
                .notification(NotificationDto.builder().title("Request join task   "+taskData.getCode()).body(users.getFullName()+" request join to the task "+taskData.getCode()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        notificationRepository.saveAll(notificationEntities);
         return "Create request join task "+ taskData.getCode() +" successfull";
    }

    public  List<TaskDTO> convert(List<Task> list){
        List<TaskDTO> taskDTOList =new ArrayList<>();
        if(DataUtils.listNotNullOrEmpty(list)){
        list.forEach(task -> {
            TaskDTO taskDTO =new TaskDTO();
            BeanUtils.copyProperties(task, taskDTO);
            Users reporter = usersService.getById(task.getReporter());
            UsersDto reporter1 = new UsersDto();
            BeanUtils.copyProperties(reporter, reporter1);
            taskDTO.setReporter(reporter1);
            taskDTO.setPriorityId(task.getPriority().getPriorityId());
            taskDTO.setParent(TaskDTO.builder().taskId(task.getTaskId()).build());
            List<TaskUsers> taskUsers1 = taskUsersRepository.findTaskUsersByTaskId(task.getTaskId());
            List<TaskUsersDTO> taskUsersDTOS = new ArrayList<>();
            List<UsersDto> userIds= new ArrayList<>();
            for (int j = 0; j < taskUsers1.size() ; j++) {
                TaskUsersDTO taskUsersDTO =new TaskUsersDTO();
                BeanUtils.copyProperties(taskUsers1.get(j), taskUsersDTO);
                Users u = usersService.getById(taskUsers1.get(j).getUserId());
                UsersDto ud = new UsersDto();
                BeanUtils.copyProperties(u, ud);
                userIds.add(ud);
                taskUsersDTO.setUserId(ud);
                taskUsersDTOS.add(taskUsersDTO);
            }
            if(Objects.nonNull(task.getSprint())){
                taskDTO.setSprintId(task.getSprint().getSprintId());
            }
            taskDTO.setUsersList(taskUsersDTOS);
            taskDTOList.add(taskDTO);
        });
        }
        return taskDTOList;
    }
    @Override
    public List<ChartResponseDto> getstatisticTask(Long projectId) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
        return taskRepository.getstatisticTask(projectId);
    }

    @Override
    public List<ReportUserTaskDTO> getReportUserTask(Long projectId, Boolean checkDoing) {
        Assert.notNull(projectId,Const.responseError.projectId_null);
        List<ReportUserTaskDTO> list= taskRepository.getReportUserTask(projectId,checkDoing);
        list.forEach(o->{
            o.getReportTaskStep().forEach(i->{
                List<Task> tasks=taskRepository.getListTaskByUserAndStep(i.getStepId(),i.getUserId());
                List<TaskDTO> t =convert(tasks);
                i.setTask(t);
            });
            o.getReportTaskLevel().forEach(i->{
                List<Task> tasks=taskRepository.getListTaskByUserAndLevel(i.getLevelId(),i.getUserId());
                List<TaskDTO> t =convert(tasks);
                i.setTask(t);
            });
        });
        return list;
    }
}
