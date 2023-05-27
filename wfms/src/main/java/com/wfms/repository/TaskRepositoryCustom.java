package com.wfms.repository;

import com.wfms.Dto.*;

import java.util.List;

public interface TaskRepositoryCustom {
    List<ChartResponseDto> getstatisticTask(Long projectId);
    List<ReportUserTaskDTO> getReportUserTask(Long projectId,Boolean checkDoing);
    List<TaskDoingDTO> getTaskDoing(Long userId);
    List<ChartTask> getTaskInProject(Long projectId);
    List<ChartTask> getTaskInProjectWithStatus(Long projectId,Integer status);
    List<DashBoard> dashBoard();
}
