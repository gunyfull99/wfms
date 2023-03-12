package com.wfms.Dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wfms.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PriorityDTO {
    private Long priorityId;
    private String priorityName;
    private Integer status;
}
