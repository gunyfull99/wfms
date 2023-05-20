package com.wfms.Dto;

import com.wfms.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportTaskLevelDTO {
    private Long userId;
    private Long levelId;
    private String level;
    private Integer countLevel;
    private List<TaskDTO> task;
}
