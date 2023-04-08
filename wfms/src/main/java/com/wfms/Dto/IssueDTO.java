package com.wfms.Dto;

import com.wfms.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueDTO {
    private Long issueId;
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
    private Long parent;
    private Integer status;
    private Long sprintId;
    private Long workFlowStepId;
    private Long priorityId;
    private Long issueTypeId;
    private Long workFlowId;
    private LocalDateTime deadline;
    private List<IssueUsersDTO> usersList;
}
