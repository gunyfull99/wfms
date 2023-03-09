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
public class ProjectUserDTO {
    private Long projectId;
    private List<Long> userId;
}
