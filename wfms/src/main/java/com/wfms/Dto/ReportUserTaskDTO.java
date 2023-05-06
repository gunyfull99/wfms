package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class ReportUserTaskDTO {
    private UsersDto user;
    private List<ReportTaskLevelDTO> reportTaskLevel;
    private List<ReportTaskStepDTO> reportTaskStep;

}
