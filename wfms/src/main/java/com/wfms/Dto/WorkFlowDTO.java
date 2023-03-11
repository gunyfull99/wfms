package com.wfms.Dto;

import com.wfms.entity.Issue;
import com.wfms.entity.WorkFlowIssueType;
import com.wfms.entity.WorkFlowStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkFlowDTO {
    private Long workFlowId;
    private Long projectId;
    private String workFlowName;
    private String descriptor;
    private Integer status;
    List<WorkFlowStep> workFlowStep;
    List<WorkFlowIssueType> workFlowIssueType;
    private Date createDate;
    private Date updateDate;
}
