package com.wfms.service;

import com.wfms.Dto.ChartTask;
import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.TaskDTO;
import com.wfms.Dto.ObjectPaging;
import com.wfms.entity.TaskUsers;
import com.wfms.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTaskByUserId(Long userId);
    List<Task> getTaskByUserIdAndProjectId(Long userId, Long projectId);
    Task createTask(String token, TaskDTO task);
    List<TaskDTO> getTaskByProjectId(Long projectId);
    TaskDTO getDetailTaskById(Long taskId);
    Task updateTask(String token , TaskDTO task);
    List<TaskUsers> updateAssignessTask(List<TaskUsers> taskUsers);
    List<TaskDTO> getListTask(Long projectId, Long sprintId);
    ObjectPaging searchTask(ObjectPaging objectPaging);
    List<List<ChartTask>> chartTask(Long projectId, Integer status);
    String requestToTask(String token, Long taskId);
    List<ChartResponseDto> getstatisticTask (Long projectId);


}
