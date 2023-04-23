package com.wfms.service.impl;

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
    @Override
    public List<Task> getTaskByUserId(Long userId) {
        return taskRepository.getTaskByUserId(userId);
    }

    @Override
    public List<Task> getTaskByUserIdAndProjectId(Long userId, Long projectId) {
        Assert.notNull(userId,"Mã nhân viên không được để trống");
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return taskRepository.getTaskByUserIdAndProjectId(userId,projectId);
    }

    @Override
    public Task createTask(String token, TaskDTO task) {
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
                i.setStatus(u.getJobTitle().equals("DEV")? 3 : 1);
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
        if(Objects.nonNull(task.getAssigness())){
            u = usersService.getUserById(task.getAssigness());
            if(role.contains("PM")){
                taskUsersService.createTaskUser(TaskUsers.builder()
                        .taskId(i.getTaskId())
                        .userId(task.getAssigness())
                        .status(2)
                        .isResponsible(u.getJobTitle().equals("DEV"))
                        .isTesterResponsible(u.getJobTitle().equals("DEV")).
                        build());
            }else{
                taskUsersService.createTaskUser(TaskUsers.builder()
                        .taskId(i.getTaskId())
                        .userId(users.getId())
                        .status(1)
                        .isResponsible(u.getJobTitle().equals("DEV"))
                        .isTesterResponsible(u.getJobTitle().equals("DEV"))
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
    public Task updateTask(String token, TaskDTO task) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        List<String> role=users.getRoles().stream().map(Roles::getName).collect(Collectors.toList());
        Assert.notNull(task.getTaskId(),"Mã công việc không được để trống");
        Task taskData = taskRepository.getById(task.getTaskId());

        Assert.notNull(taskData,"Không tìm thấy công việc");
        List<Long> workFlowStep=workFlowStepService.listWorkFlowStep(taskData.getWorkFlowId())
                        .stream().map(WorkFlowStep :: getWorkFlowStepId).collect(Collectors.toList());
        Assert.isTrue(workFlowStep.contains(task.getWorkFlowStepId()),"WorkFlowStep không trong WorkFlow hiện tại");
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
                Assert.isTrue(role.contains("PM"),"Bạn không có quyền thực hiện công việc update step");
            }
        }
        if(role.contains("PM")){
            taskData.setDescription(task.getDescription());
            taskData.setSummary(task.getSummary());
            if(Objects.nonNull(task.getStatus())){
                taskData.setStatus(task.getStatus());
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
        }
        taskData.setUpdateDate(new Date());
        taskData.setWorkFlowStepId(task.getWorkFlowStepId());
        return taskRepository.save(taskData);
    }

    @Override
    public List<TaskUsers> updateAssignessTask(List<TaskUsers> taskUser) {
        if(DataUtils.listNotNullOrEmpty(taskUser)){
            Task taskData = taskRepository.getById(taskUser.get(0).getTaskId());
            Assert.notNull(taskData,"Không tìm thấy công việc");
            List<TaskUsers>countDev=taskUser.stream().filter(TaskUsers::getIsResponsible).collect(Collectors.toList());
            List<TaskUsers>countTester=taskUser.stream().filter(TaskUsers::getIsTesterResponsible).collect(Collectors.toList());
            if(taskData.getStatus()==1){
                Assert.isTrue(countDev.size()==1 || countDev.size()==0 ,"Số DEV làm chính phải là 1");
                Assert.isTrue(countTester.size()==1 || countTester.size()==0 ,"Số TESTER làm chính phải là 1");
            }else{
                Assert.isTrue(countDev.size()==1,"Số DEV làm chính phải là 1");
                Assert.isTrue(countTester.size()==1,"Số TESTER làm chính phải là 1");
            }
            List<Long>checkTaskId=taskUser.stream().map(TaskUsers::getTaskId).distinct().collect(Collectors.toList());
            Assert.isTrue(checkTaskId.size()==1,"Danh sách member đang không cùng 1 task");

            taskUser.forEach(taskUsers -> {
                Assert.notNull(taskUsers.getTaskId(),"Mã công việc không được để trống");
                Assert.notNull(taskUsers.getUserId(),"UserId không được để trống");
                Assert.notNull(taskUsers.getIsResponsible(),"IsResponsible không được để trống");
                Assert.notNull(taskUsers.getStatus(),"Status không được để trống");
                ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(taskUsers.getUserId(), taskData.getProjectId());
                Assert.notNull(projectUsers,"Không tìm thấy member trong dự án");
                UsersDto usersDto = usersService.getUserById(taskUsers.getUserId());
                if(taskUsers.getIsResponsible()){
                    Assert.isTrue(usersDto.getJobTitle().equals("DEV"),"Tester không được làm main của DEV");
                }
                if(taskUsers.getIsTesterResponsible()){
                    Assert.isTrue(usersDto.getJobTitle().equals("TESTER"),"DEV không được làm main của TESTER");
                }
                TaskUsers taskUsers1 = taskUsersRepository.findTaskUsersByUserIdAndTaskId(taskUsers.getUserId(),taskUsers.getTaskId());
                if(Objects.nonNull(taskUsers1)){
                    taskUsers1.setStatus(taskUsers.getStatus());
                    taskUsers1.setIsResponsible(taskUsers.getIsResponsible());
                    taskUsers1.setIsTesterResponsible(taskUsers.getIsTesterResponsible());
                    taskUsers1.setUpdateDate(new Date());
                    taskUsersRepository.save(taskUsers1);
                }else{
                    taskUsersService.createTaskUser(TaskUsers.builder()
                            .taskId(taskUsers.getTaskId())
                            .userId(taskUsers.getUserId())
                            .status(taskUsers.getStatus())
                            .isResponsible(taskUsers.getIsResponsible())
                            .isTesterResponsible(taskUsers.getIsTesterResponsible())
                            .build());
                }

            });
            if(taskData.getStatus()==1 && countDev.size()==1){
                taskData.setApproveDate(new Date());
                taskData.setStatus(3);
                taskRepository.save(taskData);
            }
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
            list = taskRepository.searchTaskPaging(objectPaging.getProjectId(), objectPaging.getStatus(), objectPaging.getKeyword(),objectPaging.getSprintId(),objectPaging.getStepId(),objectPaging.getCreateByPm(),pageable);
        }
        List<TaskDTO> taskDTOList =convert(list.getContent());
        return ObjectPaging.builder().total((int) list.getTotalElements())
                .page(objectPaging.getPage())
                .limit(objectPaging.getLimit())
                .data(taskDTOList).build();
    }

    @Override
    public List<List<ChartTask>> chartTask(Long projectId, Integer status) {
        Assert.notNull(projectId,"ProjectId không được để trống");
        List<SprintDTO> sprintDTOList = sprintService.findSprintByProjectId(projectId).stream().filter(sprintDTO -> sprintDTO.getStatus()==status).collect(Collectors.toList());
        List<Task> task1;
        List<List<ChartTask>> list=new ArrayList<>();
            if(DataUtils.listNotNullOrEmpty(sprintDTOList)){
                for (int i = 0; i < sprintDTOList.size(); i++) {
                    task1 = taskRepository.getListTaskInSprint(sprintDTOList.get(i).getSprintId());
                    List<ChartTask> chartTasks =new ArrayList<>();
                    if(DataUtils.listNotNullOrEmpty(task1)){
                        List<WorkFlowStep> step=workFlowStepService.listWorkFlowStep(task1.get(0).getWorkFlowId());
                        if(DataUtils.listNotNullOrEmpty(step)){
                            int finalI = i;
                            step.forEach(workFlowStep -> {
                                List<Task> task = taskRepository.getListTaskInSprintAndStep(sprintDTOList.get(finalI).getSprintId(),workFlowStep.getWorkFlowStepId());
                                ChartTask chartTask =  ChartTask.builder()
                                        .name(workFlowStep.getWorkFLowStepName())
                                        .color(workFlowStep.getColor())
                                        .numberTask(task.size()).build();
                                chartTasks.add(chartTask);
                            });
                        }
                    }
                    list.add(chartTasks);
                }
            }
        return list ;
    }

    @Override
    public String requestToTask(String token, Long taskId) {
        String jwtToken = token.substring(7);
        String username = jwtUtility.getUsernameFromToken(jwtToken);
        Users users =usersService.getByUsername(username);
        if(users==null) return null;
        Assert.notNull(taskId,"TaskId không được để trống");
        Task taskData = taskRepository.findById(taskId).get();
        Assert.notNull(taskData,"Không tìm thấy công việc");
        ProjectUsers projectUsers = projectUsersRepository.getProjectUersByUserIdAndProjectId(users.getId(), taskData.getProjectId());
        Assert.notNull(projectUsers,"Không tìm thấy member " +users.getFullName() +" trong dự án");
        requestTaskRepository.save(RequestTask.builder()
                .requestTaskId(null)
                .taskId(taskId)
                .userId(users.getId())
                .createDate(new Date())
                .status(1).build());
         return "Tạo yêu cầu vào task "+ taskData.getCode() +" thành công";
    }

    public  List<TaskDTO> convert(List<Task> list){
        List<TaskDTO> taskDTOList =new ArrayList<>();
        list.forEach(task -> {
            TaskDTO taskDTO =new TaskDTO();
            BeanUtils.copyProperties(task, taskDTO);
            Users reporter = usersService.getById(task.getReporter());
            UsersDto reporter1 = new UsersDto();
            BeanUtils.copyProperties(reporter, reporter1);
            taskDTO.setReporter(reporter1);
            taskDTO.setPriorityId(task.getPriority().getPriorityId());
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
        return taskDTOList;
    }
    @Override
    public List<ChartResponseDto> getstatisticTask(Long projectId) {
        Assert.notNull(projectId,"Mã dự án không được để trống");
        return taskRepository.getstatisticTask(projectId);
    }
}
