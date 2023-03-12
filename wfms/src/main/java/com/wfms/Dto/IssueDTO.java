package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wfms.entity.*;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueDTO {
    private Long issueId;
    private Long assigness;
    private Long reporter;
    private String creator;
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
}
