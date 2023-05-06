package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    private Long taskId;
    private Long assigness;
    private UsersDto reporter;
    private String code;
    private String summary;
    private String description;
    private Date resolutionDate;
    private Double timeEstimate;
    private Boolean isArchived;
    private String archivedBy;
    private Date archivedDate;
    private Long projectId;
    private String approver;
    private Date approveDate;
    private TaskDTO parent;
    private Integer status;
    private Long sprintId;
    private Long workFlowStepId;
    private Long priorityId;
    private Long taskTypeId;
    private Long workFlowId;
    private Long levelDifficultId;
    private Boolean createByPm;
    private Date deadLine;
    private List<TaskUsersDTO> usersList;
}
