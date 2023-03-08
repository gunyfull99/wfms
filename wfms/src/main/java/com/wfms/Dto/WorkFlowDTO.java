package com.wfms.Dto;

import com.wfms.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.HashSet;
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
}
