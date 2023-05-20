package com.wfms.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


 import java.time.LocalDateTime; 
 import java.time.LocalDateTime; 
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
    private LocalDateTime resolutionDate;
    private Double timeEstimate;
    private Boolean isArchived;
    private String archivedBy;
    private LocalDateTime archivedDate;
    private Long projectId;
    private String approver;
    private LocalDateTime approveDate;
    private TaskDTO parent;
    private Integer status;
    private Long sprintId;
    private Long workFlowStepId;
    private Long priorityId;
    private Long taskTypeId;
    private Long workFlowId;
    private Long levelDifficultId;
    private Boolean createByPm;
    private LocalDateTime deadLine;
    private List<TaskUsersDTO> usersList;
}
