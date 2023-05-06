package com.wfms.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.wfms.Dto.*;
import com.wfms.entity.RequestTask;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTaskByUserId(Long userId);
    List<TaskDoingDTO> getTaskDoing(String token);
    List<Task> getTaskByUserIdAndProjectId(Long userId, Long projectId);
    Task createTask(String token, TaskDTO task) throws FirebaseMessagingException;
    List<TaskDTO> getTaskByProjectId(Long projectId);
    TaskDTO getDetailTaskById(Long taskId);
    Task updateTask(String token , TaskDTO task) throws FirebaseMessagingException;
    List<TaskUsers> updateAssignessTask(List<TaskUsers> taskUsers) throws FirebaseMessagingException;
    List<TaskDTO> getListTask(Long projectId, Long sprintId);
    ObjectPaging searchTask(ObjectPaging objectPaging);
    List<ChartTask> chartTask(Long projectId, Integer status);
    List<ChartTask> chartTaskInProject(Long projectId);
    String requestToTask(String token, RequestTask requestTask) throws FirebaseMessagingException;
    List<ChartResponseDto> getstatisticTask (Long projectId);
    List<ReportUserTaskDTO> getReportUserTask(Long projectId);


}
