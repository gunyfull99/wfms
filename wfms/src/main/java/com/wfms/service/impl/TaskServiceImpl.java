package com.wfms.service.impl;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.TaskDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.Dto.SprintDTO;
import com.wfms.entity.*;
import com.wfms.repository.*;
import com.wfms.repository.RequestTaskRepository;
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

import java.util.*;
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
    private NewsRepository newsRepository;
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
        return taskRepository.getTaskDoing(users.getId());
    }

    @Override
    public List<Task> getTaskByUserIdAndProjectId(Long userId, Long projectId) {
        Assert.notNull(userId,"Mã nhân viên không được để trống");
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return taskRepository.getTaskByUserIdAndProjectId(userId,projectId);
    }

    @Override
    public Task createTask(String token, TaskDTO task) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(task.getProjectId(),"Mã dự án không được để trống");
        Assert.notNull(task.getDeadLine(),"Deadline không được để trống");
        Assert.notNull(task.getPriorityId(),"Mức độ ưu tiên không được để trống");
        Assert.notNull(task.getLevelDifficultId(),"Độ khó không được để trống");
        Assert.notNull(task.getTaskTypeId(),"Loại task không được để trống");
        Projects p = projectRepository.findById(task.getProjectId()).get();
        Assert.notNull(p,"Không tìm thấy dự án với id "+ task.getProjectId());
        if(p.getStatus()==2){
            Assert.isTrue(false,"Project closed");
        }else if(p.getStatus()==1){
            Assert.isTrue(false,"Project not start");
        }else if(p.getStatus()==0){
            Assert.isTrue(false,"Project inactive");
        }
        Priority priority=priorityRepository.findById(task.getPriorityId()).get();
        Assert.notNull(priority,"Không tìm thấy priority với id "+ task.getPriorityId());
        LevelDifficult level=levelRepository.findById(task.getLevelDifficultId()).get();
        Assert.notNull(level,"Không tìm thấy level với id "+ task.getLevelDifficultId());
        TaskTypes taskTypes = taskTypeRepository.findById(task.getTaskTypeId()).get();
        Assert.notNull(taskTypes,"Không tìm thấy taskType với id "+ task.getTaskTypeId());
        WorkFlow workFlow=workFlowRepository.getDetailWorkflow(task.getProjectId());
        Assert.notNull(workFlow,"Không tìm thấy WorkFlow ");
        Integer count = taskRepository.getCountTaskByProject(p.getProjectId());
        Task i = new Task();
        BeanUtils.copyProperties(task,i);
        i.setCreatedDate(new Date());
        i.setTaskId(null);
        i.setWorkFlowId(workFlow.getWorkFlowId());
        i.setCode(p.getShortName()+"-"+ (count==null ? 1:count+1));
        i.setArchivedBy(null);
        i.setArchivedDate(null);
        i.setIsArchived(false);
        if(Objects.nonNull(task.getParent())&& Objects.nonNull(task.getParent().getTaskId())){
            Task taskData = taskRepository.getById(task.getParent().getTaskId());
            Assert.notNull(taskData,"Không tìm thấy công việc có taskId = "+task.getParent().getTaskId());
            i.setParent(taskData.getTaskId());
        }

        WorkFlowStep workFlowStep = workFlowStepRepository.getWorkFLowStepStart(workFlow.getWorkFlowId());
        Assert.notNull(workFlowStep,"Không tìm thấy step start ");
        i.setWorkFlowStepId(workFlowStep.getWorkFlowStepId());
        if(Objects.nonNull(task.getSprintId())){
            SprintDTO sprintDTO=sprintService.getDetailSprint(task.getSprintId());
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
                i.setApproveDate(new Date());
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
        List<News>newsEntitys=new ArrayList<>();
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

                newsEntitys.add(News.builder()
                        .taskId(i.getTaskId())
                        .userId(task.getAssigness())
                        .title("Add to the task "+i.getCode())
                        .description("You have been added to the task "+i.getCode())
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
                MessageDto messageDtoList =   MessageDto.builder().userId(List.of(i.getAssigness()))
                        .notification(NotificationDto.builder().title("Add to the task "+i.getCode()).body("You have been added to the task "+i.getCode()).build()).build();
                fireBaseService.sendManyNotification(messageDtoList);
                newsRepository.saveAll(newsEntitys);
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
        Assert.notNull(projectId,"Mã dự án không được để trống");
        List<Task> task = taskRepository.getTaskByProjectId(projectId);
        return convert(task);
    }

    @Override
    public TaskDTO getDetailTaskById(Long taskId) {
        Assert.notNull(taskId,"TaskID không được để trống");
        Task task = taskRepository.findById(taskId).get();
        Assert.notNull(task,"Không tìm thấy task");
        return convert(List.of(task)).get(0);
    }

    @Override
    public Task updateTask(String token, TaskDTO task) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(task.getTaskId(),"Mã công việc không được để trống");
        Task taskData = taskRepository.getById(task.getTaskId());
        Assert.notNull(taskData,"Không tìm thấy công việc có taskId = "+task.getTaskId());
        List<WorkFlowStep> workFlowSteps =workFlowStepService.listWorkFlowStep(taskData.getWorkFlowId());
        List<Long> workFlowStep=workFlowSteps
                        .stream().map(WorkFlowStep :: getWorkFlowStepId).collect(Collectors.toList());
        Assert.isTrue(workFlowStep.contains(task.getWorkFlowStepId()),"WorkFlowStep không trong WorkFlow hiện tại");
     //   List<WorkFlowStep> stepResolve=workFlowSteps.stream().filter(WorkFlowStep::getResolve).collect(Collectors.toList());
        List<WorkFlowStep> stepClose=workFlowSteps.stream().filter(WorkFlowStep::getClosed).collect(Collectors.toList());
      //  List<WorkFlowStep> stepTested=workFlowSteps.stream().filter(WorkFlowStep::getTested).collect(Collectors.toList());
     //   ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(taskData.getAssigness(),taskData.getProjectId());
      //  Assert.notNull(projectUsers,"Không tìm thấy người làm công việc nên không thể chuyển công việc");
        if(task.getSprintId()!=null){
          List<Long>sprintId= sprintService.findSprintByProjectId(taskData.getProjectId())
                  .stream().map(SprintDTO::getSprintId).collect(Collectors.toList());
          Assert.isTrue(sprintId.contains(task.getSprintId()),"Sprint không trong project hiện tại");
            taskData.setSprint(Sprint.builder().sprintId(task.getSprintId()).build());
        }
        if(!Objects.equals(taskData.getWorkFlowStepId(), task.getWorkFlowStepId())){
            if(taskData.getStatus()!=3){
                Assert.isTrue(role.contains("PM"),"Task chưa active.Bạn không có quyền thực hiện công việc update step");
            }
        }
        boolean checkChange=false;
        boolean checkChangeStep=false;
        if(role.contains("PM")){
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
                taskData.setWorkFlowStepId(task.getWorkFlowStepId());
                if(Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId())){
                    taskData.setStatus(2);
                    checkChange=true;
                }
                checkChangeStep=true;
            }
        }else{
            Assert.isTrue((!Objects.equals(stepClose.get(0).getWorkFlowStepId(), task.getWorkFlowStepId())),"Bạn không có quyền kéo sang step close task");
            TaskUsers tu=taskUsersRepository.findTaskUsersByUserIdAndTaskIdAndStatus(users.getId(),taskData.getTaskId());
           if(Objects.isNull(tu)){
               List<Task>taskList=taskRepository.getListTaskByParent(taskData.getTaskId());
               Assert.isTrue( DataUtils.listNotNullOrEmpty(taskList),"Bạn không có quyền kéo task này");
               List<TaskUsers> ts =new ArrayList<>();
               taskList.forEach(o->{
                   TaskUsers temp=taskUsersRepository.findTaskUsersByUserIdAndTaskIdAndStatus(users.getId(),o.getTaskId());
                   if(Objects.nonNull(temp)){
                       ts.add(temp);
                   }
               });
               Assert.isTrue(DataUtils.listNotNullOrEmpty(ts),"Bạn không có quyền kéo task này" );
               checkChangeStep=true;
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
        taskData.setUpdateDate(new Date());
       Task t= taskRepository.save(taskData);
       if(checkChange||checkChangeStep){
            List<Long>userId= taskUsersRepository.findUserInTask(task.getTaskId());
           List<News>newsEntitys=new ArrayList<>();
           String title="";
           String description="";
           if(checkChange){
               title="Closed task "+taskData.getCode();
               description="Task "+taskData.getCode()+" have been closed";
           }else {
               title="Change step task "+taskData.getCode();
               description="Task "+taskData.getCode()+" have been change step";
           }
           String finalTitle = title;
           String finalDescription = description;
           if(DataUtils.listNotNullOrEmpty(userId)){
               userId.forEach(o->{
                   newsEntitys.add(News.builder()
                           .taskId(t.getTaskId())
                           .userId(o)
                           .title(finalTitle)
                           .description(finalDescription)
                           .status(1)
                           .timeRecive(new Date())
                           .createDate(new Date())
                           .build());
               });
           }

           MessageDto messageDtoList =   MessageDto.builder().userId(userId)
                   .notification(NotificationDto.builder().title(finalTitle).body(finalDescription).build()).build();
           fireBaseService.sendManyNotification(messageDtoList);
           newsRepository.saveAll(newsEntitys);
       }

        return t;
    }

    @Override
    public List<TaskUsers> updateAssignessTask(List<TaskUsers> taskUser) throws FirebaseMessagingException {
        if(DataUtils.listNotNullOrEmpty(taskUser)){
            Task taskData = taskRepository.getById(taskUser.get(0).getTaskId());
            Assert.notNull(taskData,"Không tìm thấy công việc");
            List<TaskUsers>countDev=taskUser.stream().filter(TaskUsers::getIsResponsible).collect(Collectors.toList());
            //List<TaskUsers>countTester=taskUser.stream().filter(TaskUsers::getIsTesterResponsible).collect(Collectors.toList());
            if(taskData.getStatus()==1){
                Assert.isTrue(countDev.size()==1 || countDev.size()==0 ,"Số người làm chính phải là 1");
             //   Assert.isTrue(countTester.size()==1 || countTester.size()==0 ,"Số TESTER làm chính phải là 1");
            }else{
                Assert.isTrue(countDev.size()==1,"Số người làm chính phải là 1");
               // Assert.isTrue(countTester.size()==1 || countTester.size()==0,"Số TESTER làm chính phải là 1");
            }
            List<Long>userIds=taskUser.stream().map(TaskUsers::getUserId).collect(Collectors.toList());
            List<Long>checkTaskId=taskUser.stream().map(TaskUsers::getTaskId).distinct().collect(Collectors.toList());
            Assert.isTrue(checkTaskId.size()==1,"Danh sách member đang không cùng 1 task");
            List<News> newsEntitys= new ArrayList<>();
            taskUser.forEach(taskUsers -> {
                Assert.notNull(taskUsers.getTaskId(),"Mã công việc không được để trống");
                Assert.notNull(taskUsers.getUserId(),"UserId không được để trống");
                Assert.notNull(taskUsers.getIsResponsible(),"IsResponsible không được để trống");
                Assert.notNull(taskUsers.getStatus(),"Status không được để trống");
                ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(taskUsers.getUserId(), taskData.getProjectId());
                Assert.notNull(projectUsers,"Không tìm thấy member trong dự án");
                UsersDto usersDto = usersService.getUserById(taskUsers.getUserId());
//                if(taskUsers.getIsResponsible()){
//                    Assert.isTrue(usersDto.getJobTitle().equals("DEV"),"Tester không được làm main của DEV");
//                }
//                if(taskUsers.getIsTesterResponsible()){
//                    Assert.isTrue(usersDto.getJobTitle().equals("TESTER"),"DEV không được làm main của TESTER");
//                }
                TaskUsers taskUsers1 = taskUsersRepository.findTaskUsersByUserIdAndTaskId(taskUsers.getUserId(),taskUsers.getTaskId());
                if(Objects.nonNull(taskUsers1)){
                    taskUsers1.setStatus(taskUsers.getStatus());
                    taskUsers1.setIsResponsible(taskUsers.getIsResponsible());
                 //   taskUsers1.setIsTesterResponsible(taskUsers.getIsTesterResponsible());
                    taskUsers1.setUpdateDate(new Date());
                    taskUsersRepository.save(taskUsers1);
                    if(taskUsers.getIsResponsible()) {
                        taskData.setAssigness(taskUsers.getUserId());
                    }
                }else{
                    taskUsersService.createTaskUser(TaskUsers.builder()
                            .taskId(taskUsers.getTaskId())
                            .userId(taskUsers.getUserId())
                            .status(taskUsers.getStatus())
                            .isResponsible(taskUsers.getIsResponsible())
                         //   .isTesterResponsible(taskUsers.getIsTesterResponsible())
                            .build());
                    if(taskUsers.getIsResponsible()) {
                        taskData.setAssigness(taskUsers.getUserId());
                    }
                }
                newsEntitys.add(News.builder()
                        .taskId(taskUsers.getTaskId())
                        .userId(taskUsers.getUserId())
                        .title(taskUsers.getStatus()==2 ? (taskUsers.getIsResponsible() ? "Mạin" : "Added")+" to task  "+taskData.getCode() :"Remove from task  "+taskData.getCode() )
                        .description(taskUsers.getStatus()==2 ? "You have been "+ (taskUsers.getIsResponsible() ? "mạin" : "added") +" to task "+taskData.getCode() :"You have been remove from task "+taskData.getCode())
                        .status(1)
                        .timeRecive(new Date())
                        .createDate(new Date())
                        .build());
            });
            if(taskData.getStatus()==1 && countDev.size()==1){
                taskData.setApproveDate(new Date());
                taskData.setStatus(3);
            }
            taskRepository.save(taskData);
            taskUser.forEach(taskUsers -> {
                        MessageDto messageDtoList =   MessageDto.builder().userId(userIds)
                                .notification(NotificationDto.builder().title(taskUsers.getStatus()==2 ? (taskUsers.getIsResponsible() ? "Mạin" : "Added")+" to task  "+taskData.getCode() :"Remove from task  "+taskData.getCode())
                                        .body(taskUsers.getStatus()==2 ? "You have been "+ (taskUsers.getIsResponsible() ? "mạin" : "added") +" to task "+taskData.getCode() :"You have been remove from task "+taskData.getCode()).build()).build();
                try {
                    fireBaseService.sendManyNotification(messageDtoList);
                } catch (FirebaseMessagingException e) {
                        Assert.isTrue(true,"Have error in firebase");
                }
            });

            newsRepository.saveAll(newsEntitys);
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
    public ObjectPaging searchTask(ObjectPaging objectPaging) {
        Pageable pageable = PageRequest.of(objectPaging.getPage() - 1, objectPaging.getLimit(), Sort.by("taskId").descending());
        Page<Task> list;
        if(Objects.nonNull(objectPaging.getSprintId()) && objectPaging.getSprintId()== -1){
            list = taskRepository.searchTaskPagingBackLog(objectPaging.getProjectId(), objectPaging.getStatus(), objectPaging.getKeyword(),objectPaging.getSprintId(),objectPaging.getStepId(),objectPaging.getCreateByPm(),pageable);
        }else{
            list = taskRepository.searchTaskPaging(objectPaging.getProjectId(),
                    objectPaging.getStatus(),
                    objectPaging.getKeyword(),
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
        Assert.notNull(projectId,"ProjectId không được để trống");
        return taskRepository.getTaskInProjectWithStatus(projectId,status);
    }

    @Override
    public List<ChartTask> chartTaskInProject(Long projectId) {
        Assert.notNull(projectId,"ProjectId không được để trống");
        return taskRepository.getTaskInProject(projectId);
    }

    @Override
    public String requestToTask(String token, RequestTask requestTask) throws FirebaseMessagingException {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(requestTask.getTaskId(),"TaskId không được để trống");
        Assert.notNull(requestTask.getReason(),"Reason không được để trống");
        Task taskData = taskRepository.findById(requestTask.getTaskId()).get();
        Assert.notNull(taskData,"Không tìm thấy công việc");
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(users.getId(), taskData.getProjectId());
        Assert.notNull(projectUsers,"Không tìm thấy member " +users.getFullName() +" trong dự án");
        List<News> newsEntitys = new ArrayList<>();
        requestTaskRepository.save(RequestTask.builder()
                .requestTaskId(null)
                .taskId(requestTask.getTaskId())
                .reason(requestTask.getReason())
                .userId(users.getId())
                .createDate(new Date())
                .status(1).build());
        newsEntitys.add(News.builder()
                .taskId(requestTask.getTaskId())
                .userId(taskData.getReporter())
                .title("Request join task  "+taskData.getCode())
                .description(users.getFullName()+" request join to the task "+taskData.getCode())
                .status(1)
                .timeRecive(new Date())
                .createDate(new Date())
                .build());
        MessageDto messageDtoList =   MessageDto.builder().userId(List.of(taskData.getReporter()))
                .notification(NotificationDto.builder().title("Request join task   "+taskData.getCode()).body(users.getFullName()+" request join to the task "+taskData.getCode()).build()).build();
        fireBaseService.sendManyNotification(messageDtoList);
        newsRepository.saveAll(newsEntitys);
         return "Tạo yêu cầu vào task "+ taskData.getCode() +" thành công";
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
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return taskRepository.getstatisticTask(projectId);
    }

    @Override
    public List<ReportUserTaskDTO> getReportUserTask(Long projectId) {
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return taskRepository.getReportUserTask(projectId);
    }
}
