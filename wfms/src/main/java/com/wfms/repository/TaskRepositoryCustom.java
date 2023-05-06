package com.wfms.repository;

import com.wfms.Dto.ChartResponseDto;
import com.wfms.Dto.ChartTask;
import com.wfms.Dto.ReportUserTaskDTO;
import com.wfms.Dto.TaskDoingDTO;

import java.util.List;

public interface TaskRepositoryCustom {
    List<ChartResponseDto> getstatisticTask(Long projectId);
    List<ReportUserTaskDTO> getReportUserTask(Long projectId);
    List<TaskDoingDTO> getTaskDoing(Long userId);
    List<ChartTask> getTaskInProject(Long projectId);
    List<ChartTask> getTaskInProjectWithStatus(Long projectId,Integer status);
}
