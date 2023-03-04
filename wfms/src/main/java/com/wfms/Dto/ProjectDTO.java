package com.wfms.Dto;

import com.wfms.entity.ProjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ProjectDTO {
    private Long projectId;
    private String projectName;
    private String key;
    private String lead;
    private String description;
    private Set<ProjectType> projectType;
    private List<Long> userId;
}
